"use client";

import { useRouter } from "next/navigation";

import { Reservation } from "@/types/reservation";
import { Hotel } from "@/types/hotel";

import { MapPinIcon } from "@heroicons/react/24/solid";
import { ClockIcon } from "@heroicons/react/24/solid";

import { useSearchParams } from "next/navigation";
import { useState, useEffect, useMemo } from "react";
import { Skeleton } from "@/components/ui/skeleton";

import { toast } from "sonner";

import {
  Field,
  FieldDescription,
  FieldGroup,
  FieldLabel,
  FieldLegend,
  FieldSet,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Item,
  ItemContent,
  ItemDescription,
} from "@/components/ui/item";

import { Suspense } from "react";

interface RazorpayResponse {
  razorpay_payment_id: string;
  razorpay_order_id: string;
  razorpay_signature: string;
}


interface RazorpayOptions {
  key: string;
  order_id: string;
  amount: number;
  currency: string;
  name: string;
  description: string;
  prefill: {
    name: string;
    email: string;
    contact: string;
  };
  handler: (response: RazorpayResponse) => void;
  modal: {
    ondismiss: () => void;
  };
  theme: {
    color: string;
  };
}

interface RazorpayInstance {
  open: () => void;
}

const formatRemaining = (ms: number) => {
  if (ms <= 0) return "0s";
  const totalSecs = Math.floor(ms / 1000);
  const days = Math.floor(totalSecs / 86400);
  const hours = Math.floor((totalSecs % 86400) / 3600);
  const minutes = Math.floor((totalSecs % 3600) / 60);
  const seconds = totalSecs % 60;
  
  if (days > 0) return `${days}d ${hours}h`;
  if (hours > 0) return `${hours}h ${String(minutes).padStart(2, "0")}m ${String(seconds).padStart(2, "0")}s`;
  return `${minutes}m ${String(seconds).padStart(2, "0")}s`;
};

const paymentAPIUrl = process.env.NEXT_PUBLIC_PAYMENT_API_BASE_URL || "http://localhost:8086";
const reservationAPIUrl = process.env.NEXT_PUBLIC_RESERVATION_API_BASE_URL || "http://localhost:8085";
const hotelAPIUrl = process.env.NEXT_PUBLIC_HOTEL_API_BASE_URL || "http://localhost:8081";

function ReviewPageContent() {

  const router = useRouter();

  const searchParams = useSearchParams();
  const reservationId = searchParams.get("reservationId") ?? "";

  const [reservation, setReservation] = useState<Reservation | null>(null);
  //const [loading, setLoading] = useState<boolean>(false);
  //const [error, setError] = useState<string | null>(null);

  // hold expiry timer state
  const [timeLeftMs, setTimeLeftMs] = useState<number | null>(null);
  const [holdExpired, setHoldExpired] = useState<boolean>(false);

  // hotel details after reservation is loaded
  const [hotel, setHotel] = useState<Hotel | null>(null);
  //const [hotelLoading, setHotelLoading] = useState<boolean>(false);
  //const [hotelError, setHotelError] = useState<string | null>(null);

  // guest input state (controlled inputs)
  const [guestName, setGuestName] = useState<string>("");
  const [guestEmail, setGuestEmail] = useState<string>("");
  const [guestPhone, setGuestPhone] = useState<string>("");

  // payment state
  const [creatingPayment, setCreatingPayment] = useState<boolean>(false);
  const [paymentError, setPaymentError] = useState<string | null>(null);

  useEffect(() => {
    if(!reservationId) return;

    const abort = new AbortController();
    const fetchReservation = async () => {
      //setLoading(true);
      //setError(null);
      try {
        const res = await fetch(`${reservationAPIUrl}/api/reservations/${reservationId}`, {
          method: "GET",
          signal: abort.signal,
          headers: { Accept: "application/json" }
        });

        if(!res.ok) {
          const errBody = await res.json().catch(() => null);
          throw new Error(errBody?.message ?? `Failed to fetch reservation: ${res.status}`);
        }

        const data = await res.json();
        setReservation(data);

      } catch (err: unknown) {
        //if (err instanceof Error && err.name !== "AbortError") setError(err.message ?? "Failed to load reservation");
      } finally {
        //setLoading(false);
      }
    };

    fetchReservation();
    return () => abort.abort();
  }, [reservationId]);

  useEffect(() => {
    if(!reservation?.expiresAt) {
      setTimeLeftMs(null);
      setHoldExpired(false);
      return;
    }

    const target = new Date(reservation.expiresAt).getTime();
    const tick = () => {
      const diff = target - Date.now();
      if(diff <= 0) {
        setTimeLeftMs(0);
        setHoldExpired(true);
        return;
      }
      setTimeLeftMs(diff);
      setHoldExpired(false);
    };

    tick();
    const id = window.setInterval(tick, 1000);
    return () => window.clearInterval(id);
  }, [reservation?.expiresAt]);

  // compute number of nights 
  const nights = useMemo(() => {
    if (!reservation) return;
    const checkIn = new Date(reservation.checkInDate);
    const checkOut = new Date(reservation.checkOutDate);
    const ms = checkOut.getTime() - checkIn.getTime();
    return Math.round(ms / (1000 * 60 * 60 * 24));
  }, [reservation]);

  // global currency formatter for this component (recreated only when currency changes)
  const fmt = useMemo(() => {
    const currency = reservation?.currency ?? "INR";
    return new Intl.NumberFormat("en-IN", { 
      style: "currency", 
      currency,
      minimumFractionDigits:0,
      maximumFractionDigits:0 
    });
  }, [reservation?.currency]);

  // taxes (18%) from reservation.amount
  const taxes = useMemo(() => {
    const amt = reservation?.amount ?? 0;
    // calculate 18% and round to nearest whole unit
    return Math.round(amt * 0.18);
  }, [reservation?.amount]);

  const total = useMemo(() => {
    const amt = reservation?.amount ?? 0;
    return amt + taxes;
  }, [reservation?.amount, taxes]);

  // fetch hotel details once reservation is available (use reservation.hotelId)
  useEffect(() => {
    if(!reservation?.hotelId) return;

    const abort = new AbortController();
    const fetchHotel = async () => {
      //setHotelLoading(true);
      //setHotelError(null);

      try {
        const res = await fetch(`${hotelAPIUrl}/api/hotels/${encodeURIComponent(reservation.hotelId)}`, {
          method: "GET",
          signal: abort.signal,
          headers: { Accept: "application/json" }
        });
        
        if(!res.ok) {
          const errBody = await res.json().catch(() => null);
          throw new Error(errBody?.message ?? `Failed to fetch hotel: ${res.status}`);
        }

        const data = await res.json();
        setHotel(data);
      } catch (err: unknown) {
        //if (err instanceof Error && err.name !== "AbortError") setHotelError(err.message ?? "Failed to load hotel");

      } finally {
        //setHotelLoading(false);

      } 

    };

    fetchHotel();
    return () => abort.abort();
  }, [reservation?.hotelId]);

  const loadRazorpay = () => new Promise<void>((resolve, reject) => {
    if (typeof window !== 'undefined' && 'Razorpay' in window) return resolve();
    const script = document.createElement("script");
    script.src = "https://checkout.razorpay.com/v1/checkout.js";
    script.async = true;
    script.onload = () => resolve();
    script.onerror = () => reject(new Error("Failed to load Razorpay checkout script"));
    document.body.appendChild(script);
  });

  const handlePayNow = async () => {
    if(!reservation) return;

    setPaymentError(null);
    setCreatingPayment(true);

    // validation: require guest details
    if( !guestName?.trim() || !guestEmail?.trim() || !guestPhone?.trim() ) {
      setPaymentError("Name, Email and Phone are required");
      setCreatingPayment(false);
      return;
    }

    try {

      const payload = {
        reservationId: reservation.id,
        holdId: reservation.holdId,
        amount: Math.round((total ?? 0)),
        currency: reservation.currency ?? "INR",
        guestName: guestName,
        guestEmail: guestEmail,
        guestPhone: guestPhone   
      };

      const res = await fetch(`${paymentAPIUrl}/api/payments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      if(!res.ok) {
        const errBody = await res.json().catch(() => null);
        const message = errBody?.message ?? `Create payment failed: ${res.status}`;
        throw new Error(message);
      }

      const data = await res.json();
      const orderId = data.providerOrderId;
      const key = data.providerKeyId;
      const amountPaise = data.amount ?? Math.round((reservation.amount ?? 0) * 100);
      const currency = data.currency ?? "INR";

      if(!orderId || !key) throw new Error("Payment service did not return provider order id or key");

      await loadRazorpay();

      const options: RazorpayOptions = {
        key,
        order_id: orderId,
        amount: amountPaise,
        currency: currency,
        name: "Nivara",
        description: "Test transaction",
        prefill: {
          name: guestName || "",
          email: guestEmail || "",
          contact: guestPhone|| ""
        },
        handler: (razorpayResponse: RazorpayResponse) => {
          (async () => {
            try {
              const finalizeRes = await fetch(
                `${paymentAPIUrl}/api/payments/confirm`,
                {
                  method: "POST",
                  headers: { "Content-Type": "application/json" },
                  body: JSON.stringify({
                    reservationId: reservation.id,
                    holdId: reservation.holdId,
                    providerPaymentId: razorpayResponse.razorpay_payment_id,
                    providerOrderId: razorpayResponse.razorpay_order_id,
                    providerSignature: razorpayResponse.razorpay_signature
                  })
                }
              );

              if (!finalizeRes.ok) {
                const errBody = await finalizeRes.json().catch(() => null);
                throw new Error(errBody?.message ?? `Reservation finalize failed: ${finalizeRes.status}`);
              }

              const finalizeData = await finalizeRes.json();
              const paramString = new URLSearchParams({ 
                reservationId: finalizeData.reservationId,
                paymentId: finalizeData.paymentId 
              }).toString();

              router.push(`/bookings/confirmation?${paramString}`);
              
            } catch (err: unknown) {
              console.error("Failed to finalize reservation after payment:", err);
              setPaymentError("Failed to finalize reservation after payment: " + err);
            } finally {
              setCreatingPayment(false);
            }
          })();
        },
        modal: {
          ondismiss: () => {
            toast("Payment not completed. Checkout modal was closed by the user");
          }
        },
        theme: { color: "#111827" }
      };

      const Razorpay = (window as unknown as { Razorpay: new (options: RazorpayOptions) => RazorpayInstance }).Razorpay;
      const rzp = new Razorpay(options);
      //const rzp = new (window as unknown as { Razorpay: any }).Razorpay(options);
      rzp.open();
      
    } catch (err: unknown) {
      console.error(err);
      setPaymentError(err instanceof Error ? err.message : "Failed to initiate payment");
    } finally {
      setCreatingPayment(false);
    }

  };

  return (
    <main className="min-h-screen pt-20 pb-10 px-4 sm:px-6 lg:px-10">
      <div className="max-w-6xl mx-auto">

        {/* Hold expiry timer - responsive positioning */}
        {timeLeftMs !== null && !holdExpired && (
          <Item variant="outline" className="fixed bottom-4 right-4 sm:bottom-6 sm:right-6 z-50 w-56 sm:w-60 bg-white shadow-lg">
            <ItemContent>
              <ItemDescription className="font-medium text-gray-800 text-xs sm:text-sm">
                <ClockIcon className="w-4 h-4 sm:w-5 sm:h-5 mr-1.5 inline" />
                {`Hold expires in ${formatRemaining(timeLeftMs)}`}
              </ItemDescription>
            </ItemContent>
          </Item>
        )}

        {/* Header */}
        <header id="title" className="mb-6">
          <h1 className="text-2xl sm:text-3xl font-semibold">Booking Review</h1>
          <p className="text-xs sm:text-sm text-gray-500 mt-1">Confirm your selection and proceed to payment</p>
        </header>

        {/* Main content - responsive grid */}
        <div id="main-content" className="flex flex-col lg:grid lg:grid-cols-3 gap-6">

          {/* Left column - main content, order-2 on mobile (payment summary first) */}
          <section id="hero-section" className="lg:col-span-2 space-y-4 order-1">

            {/* Hotel Details - responsive card */}
            <div
              id="booking-summary"
              className="p-4 sm:p-5 border rounded-lg bg-white flex flex-col sm:flex-row gap-4 items-start"
            >
              <img
                src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
                alt="Hotel thumbnail"
                className="w-full sm:w-28 h-32 sm:h-20 rounded-md object-cover flex-shrink-0"
              />

              <div className="flex-1 min-w-0 w-full">
                <div className="flex flex-col sm:flex-row items-start justify-between gap-4">

                  <div id="hotel-details" className="min-w-0 flex-1">
                    <h2 className="text-base sm:text-lg font-semibold truncate">
                      {hotel?.name ?? <Skeleton className="h-4 w-full sm:w-[250px]" />}
                    </h2>
                    <div className="mt-2 flex flex-wrap items-center gap-2 sm:gap-3 text-xs text-gray-500">
                      <span className="inline-flex items-center gap-1">
                        <MapPinIcon className="w-3 h-3 sm:w-4 sm:h-4" />
                        <span className="truncate">{hotel?.address ?? <Skeleton className="h-4 w-[100px]" />}</span>
                      </span>
                      {hotel?.rating ? 
                        <span className="inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium bg-yellow-50 text-yellow-800">
                          {Number(hotel.rating).toFixed(1)} ★
                        </span>
                        :
                        <Skeleton className="h-4 w-[50px]" />
                      }
                    </div>
                  </div>

                  <div className="text-left sm:text-right flex-shrink-0 w-full sm:w-auto">
                    <div className="text-xs sm:text-sm text-gray-500">Dates</div>
                    <div className="text-xs sm:text-sm my-1 font-medium">
                      {reservation?.checkInDate ? 
                        `${reservation?.checkInDate} → ${reservation?.checkOutDate}`
                        :
                        <Skeleton className="h-4 w-full sm:w-[100px]" /> 
                      }
                    </div>
                    {typeof nights === "number" ? (
                      <div className="text-xs text-gray-400 mt-1">
                        {nights} {nights === 1 ? "night" : "nights"}
                      </div>
                    ) : (
                      <Skeleton className="h-4 w-[50px] mt-1" />
                    )}
                  </div>

                </div>
              </div>

            </div>

            {/* Rooms Summary */}
            <div id="rooms-summary" className="p-4 border rounded-lg bg-white">
              <h3 className="text-sm sm:text-md font-medium mb-3">Selected rooms</h3>
              <ul className="divide-y">
                {reservation?.reservedItems && reservation.reservedItems.length > 0 ? (
                  reservation.reservedItems.map((item, idx) => {
                    const name = item.name ?? "Unknown Room Type"
                    const quantity = item.quantity ?? 0;
                    const rate = item.rate ?? 0;

                    return (
                      <li key={idx} className="py-3 flex justify-between items-center gap-4">
                        <div className="min-w-0 flex-1">
                          <div className="font-medium text-sm sm:text-base truncate">{name}</div>
                          <div className="text-xs text-gray-500">{quantity} {quantity === 1 ? "room" : "rooms"} • Non-refundable</div>
                        </div>
                        <div className="text-right flex-shrink-0">
                          <div className="font-semibold text-sm sm:text-base">{fmt.format(rate)}</div>
                          <div className="text-xs text-gray-400">per night</div>
                        </div>
                      </li>

                    );
                  })
                ) : (
                  <li className="py-3 text-sm text-gray-500">No rooms selected</li>
                )}
              </ul>
            </div>

            {/* Guest Details - responsive form */}
            <div id="guest-details" className="p-4 border rounded-lg bg-white">

              <FieldSet>

                <FieldLegend className="text-sm sm:text-base">Guest details</FieldLegend>
                <FieldDescription className="text-xs sm:text-sm">Primary guest information — used for check-in and confirmation</FieldDescription>
                
                <FieldGroup className="mt-4 grid grid-cols-1 sm:grid-cols-2 gap-4 text-sm sm:text-md text-gray-800">
                  
                  <Field className="sm:col-span-2">
                    <FieldLabel htmlFor="username">Name</FieldLabel>
                    <FieldDescription className="text-xs">Enter your full name exactly as shown on your ID.</FieldDescription>
                    <Input 
                      id="username" 
                      type="text" 
                      placeholder="Max Leiter" 
                      value={guestName}
                      onChange={ (e) => setGuestName(e.target.value) }
                      className="text-sm sm:text-base"
                    />
                  </Field>

                  <Field>
                    <FieldLabel htmlFor="email">Email</FieldLabel>
                    <FieldDescription className="text-xs">We&apos;ll send confirmation to this email.</FieldDescription>
                    <Input 
                      id="email" 
                      type="email" 
                      placeholder="yourname@example.com" 
                      value={guestEmail}
                      onChange={ (e) => setGuestEmail(e.target.value) }
                      className="text-sm sm:text-base"
                    />
                  </Field>

                  <Field>
                    <FieldLabel htmlFor="phone">Phone</FieldLabel>
                    <FieldDescription className="text-xs">Use digits and spaces only.</FieldDescription>
                    <Input 
                      id="phone" 
                      type="tel" 
                      placeholder="+91 888 888 8888" 
                      value={guestPhone}
                      onChange={ (e) => setGuestPhone(e.target.value) }
                      className="text-sm sm:text-base"
                    />
                  </Field>

                </FieldGroup>

              </FieldSet>

              <div className="mt-6 text-xs text-gray-400">
                Note: Valid ID will be required at check-in.
              </div>

            </div>

          </section>

          {/* Payment Summary - sticky on desktop, first on mobile */}
          <aside id="side-section" className="lg:col-span-1 order-2">
            <div className="p-4 border rounded-lg bg-white lg:sticky lg:top-24 shadow-none">
              <h3 className="text-base sm:text-lg font-semibold mb-2">Payment summary</h3>

              <div className="text-xs sm:text-sm text-gray-600 mb-4">
                <div className="flex justify-between">
                  <span>Subtotal</span>
                  <span className="font-medium">{reservation?.amount ? fmt.format(reservation.amount) : <Skeleton className="h-4 w-[50px]" /> }</span>
                </div>
                <div className="flex justify-between mt-2">
                  <span>Taxes (est.)</span>
                  <span className="font-medium">{ reservation ? fmt.format(taxes) : <Skeleton className="h-4 w-[50px]" /> }</span>
                </div>
              </div>

              <div className="border-t pt-3 mb-4">

                <div className="flex justify-between items-start gap-4">

                  <div>
                    <div className="text-xs sm:text-sm text-gray-500">Total (incl. taxes)</div>
                    <div className="text-lg sm:text-xl my-2 font-semibold text-gray-900">{reservation ? fmt.format(total) : <Skeleton className="h-6 w-[120px]" />}</div>
                    <div className="text-xs text-gray-400">Includes estimated taxes & fees</div>
                  </div>

                  <div className="text-right flex-shrink-0">
                    <span className="inline-flex items-center gap-1 sm:gap-2 text-xs text-gray-500">
                      <svg className="w-3 h-3 sm:w-4 sm:h-4 text-green-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" aria-hidden="true">
                        <path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 2v6m0 8v6M4 12h16" />
                      </svg>
                      <span className="hidden sm:inline">Secure payment</span>
                      <span className="sm:hidden">Secure</span>
                    </span>
                  </div>

                </div>

              </div>

              <Button 
                id="pay-now"
                type="submit" 
                className="w-full cursor-pointer text-sm sm:text-base"
                onClick={handlePayNow}
                disabled={holdExpired || creatingPayment}

              >
                {creatingPayment ? "Processing…" : holdExpired ? "Hold Expired" : "Pay Now"}
              </Button>
              {paymentError && <div className="mt-2 text-xs sm:text-sm text-red-600">{paymentError}</div>}
            
            </div>

          </aside>

        </div>

      </div>
    </main>
  );
}

export default function ReviewPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen pt-24 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto" />
            <p className="mt-4 text-gray-600">Loading booking details...</p>
          </div>
        </div>
      }
    >
     <ReviewPageContent />
    </Suspense>
  );
}