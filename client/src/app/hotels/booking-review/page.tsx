"use client";

import { Reservation } from "@/types/reservation";
import { Hotel } from "@/types/hotel";

import { MapPinIcon } from "@heroicons/react/24/solid";
import { ExclamationTriangleIcon } from "@heroicons/react/24/solid";
import { ClockIcon } from "@heroicons/react/24/solid";

import { useSearchParams } from "next/navigation";
import { useState, useEffect, useMemo } from "react";
import { Skeleton } from "@/components/ui/skeleton";

import {
  Field,
  FieldContent,
  FieldDescription,
  FieldError,
  FieldGroup,
  FieldLabel,
  FieldLegend,
  FieldSeparator,
  FieldSet,
  FieldTitle,
} from "@/components/ui/field";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Item,
  ItemActions,
  ItemContent,
  ItemDescription,
  ItemTitle,
} from "@/components/ui/item"

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

export default function ReviewPage() {

  const searchParams = useSearchParams();
  const reservationId = searchParams.get("reservationId") ?? "";

  const [reservation, setReservation] = useState<Reservation | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // hold expiry timer state
  const [timeLeftMs, setTimeLeftMs] = useState<number | null>(null);
  const [holdExpired, setHoldExpired] = useState<boolean>(false);

  // hotel details after reservation is loaded
  const [hotel, setHotel] = useState<Hotel | null>(null);
  const [hotelLoading, setHotelLoading] = useState<boolean>(false);
  const [hotelError, setHotelError] = useState<string | null>(null);

  // guest input state (controlled inputs)
  const [guestName, setGuestName] = useState<string>("");
  const [guestEmail, setGuestEmail] = useState<string>("");
  const [guestPhone, setGuestPhone] = useState<string>("");

  useEffect(() => {
    if(!reservationId) return;

    const abort = new AbortController();
    const fetchReservation = async () => {
      setLoading(true);
      setError(null);
      try {
        const baseUrl = process.env.NEXT_PUBLIC_RESERVATION_SERVICE_URL || "http://localhost:8084";
        const res = await fetch(`${baseUrl}/api/reservations/${reservationId}`, {
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

      } catch (err: any) {
        if (err.name !== "AbortError") setError(err.message ?? "Failed to load reservation");
      } finally {
        setLoading(false);
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
  }, [reservation?.checkInDate, reservation?.checkOutDate]);

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
      setHotelLoading(true);
      setHotelError(null);

      try {
        const baseUrl = process.env.NEXT_PUBLIC_HOTEL_SERVICE_URL || "http://localhost:8081";
        const res = await fetch(`${baseUrl}/api/hotels/${encodeURIComponent(reservation.hotelId)}`, {
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
      } catch (err: any) {
        if (err.name !== "AbortError") setHotelError(err.message ?? "Failed to load hotel");

      } finally {
        setHotelLoading(false);

      } 

    };

    fetchHotel();
    return () => abort.abort();
  }, [reservation?.hotelId]);

  return (
    <main className="mt-16 max-w-6xl mx-auto p-6">

      {/* Hold expiry timer (bottom-right) */}
      {timeLeftMs !== null && !holdExpired && (
        <Item variant="outline" className="fixed bottom-6 right-6 z-50 w-60 bg-white">
          <ItemContent>
            <ItemDescription className="font-medium text-gray-800">
              <ClockIcon className="w-5 h-5 mr-1.5 inline" />
              {`Hold will expire in ${formatRemaining(timeLeftMs)}`}
            </ItemDescription>
          </ItemContent>
        </Item>
      )}

      <header id="title" className="mb-6">
        <h1 className="text-3xl font-semibold">Booking Review</h1>
        <p className="text-sm text-gray-500 mt-1">Confirm your selection and proceed to payment</p>
      </header>

      <div id="main-content" className="grid grid-cols-3 gap-6">

        <section id="hero-section" className="col-span-2 space-y-4">

          {/* Hotel Details */}
          <div
            id="booking-summary"
            className="p-5 border rounded-lg bg-white flex flex-col sm:flex-row gap-4 items-start"
          >
            <img
              src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
              alt="Hotel thumbnail"
              className="w-full sm:w-28 h-20 rounded-md object-cover flex-shrink-0"
            />

            <div className="flex-1 min-w-0">
              <div className="flex items-start justify-between gap-4">

                <div id="hotel-details" className="min-w-0">
                  <h2 className="text-lg font-semibold truncate">
                    {hotel?.name ?? <Skeleton className="h-4 w-[250px]" />}
                  </h2>
                  <div className="mt-2 flex items-center gap-3 text-xs text-gray-500">
                    <span className="inline-flex items-center gap-1">
                      <MapPinIcon className="w-4 h-4" />
                      {hotel?.address ?? <Skeleton className="h-4 w-[100px]" />}
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

                <div className="text-right flex-shrink-0">
                  <div className="text-sm text-gray-500">Dates</div>
                  <div className="text-sm my-1 font-medium">
                    {reservation?.checkInDate ? 
                      `${reservation?.checkInDate} → ${reservation?.checkOutDate}`
                      :
                      <Skeleton className="h-4 w-[100px]" /> 
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
            <h3 className="text-md font-medium mb-3">Selected rooms</h3>
            <ul className="divide-y">
              {reservation?.reservedItems && reservation.reservedItems.length > 0 ? (
                reservation.reservedItems.map((item, idx) => {
                  const name = item.name ?? "Unknown Room Type"
                  const quantity = item.quantity ?? 0;
                  const rate = item.rate ?? 0;

                  return (
                    <li key={idx} className="py-3 flex justify-between items-center">
                      <div>
                        <div className="font-medium">{name}</div>
                        <div className="text-xs text-gray-500">{quantity} {quantity === 1 ? "room" : "rooms"} • Non-refundable</div>
                      </div>
                      <div className="text-right">
                        <div className="font-semibold">{fmt.format(rate)}</div>
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

          {/* Guest Details */}
          <div id="guest-details" className="p-4 border rounded-lg bg-white">

            <FieldSet>

              <FieldLegend>Guest details</FieldLegend>
              <FieldDescription>Primary guest information — used for check-in and confirmation</FieldDescription>
              
              <FieldGroup className="mt-2 grid grid-cols-2 gap-y-3 gap-x-6 text-md text-gray-800">
                
                <Field>
                  <FieldLabel htmlFor="username">Name</FieldLabel>
                  <FieldDescription>Enter your full name exactly as shown on your ID.</FieldDescription>
                  <Input 
                    id="username" 
                    type="text" 
                    placeholder="Max Leiter" 
                    value={guestName}
                    onChange={ (e) => setGuestName(e.target.value) }
                  />
                </Field>

                <Field>
                  <FieldLabel htmlFor="email">Email</FieldLabel>
                  <FieldDescription>We'll send confirmation to this email.</FieldDescription>
                  <Input 
                    id="email" 
                    type="email" 
                    placeholder="yourname@example.com" 
                    value={guestEmail}
                    onChange={ (e) => setGuestEmail(e.target.value) }
                  />
                </Field>

                <Field>
                  <FieldLabel htmlFor="phone">Phone</FieldLabel>
                  <FieldDescription>Use digits and spaces only.</FieldDescription>
                  <Input 
                    id="phone" 
                    type="tel" 
                    placeholder="+91 888 888 8888" 
                    value={guestPhone}
                    onChange={ (e) => setGuestPhone(e.target.value) }
                  />
                </Field>

              </FieldGroup>

            </FieldSet>

            <div className="mt-6 text-xs text-gray-400">
              Note: Valid ID will be required at check-in.
            </div>

          </div>

        </section>

        {/* Payment Summary */}
        <aside id="side-section" className="col-span-1">
          <div className="p-4 border rounded-lg bg-white sticky top-24">
            <h3 className="text-lg font-semibold mb-2">Payment summary</h3>

            <div className="text-sm text-gray-600 mb-4">
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
                  <div className="text-sm text-gray-500">Total (incl. taxes)</div>
                  <div className="text-xl my-2 font-semibold text-gray-900">{reservation ? fmt.format(total) : <Skeleton className="h-6 w-[120px]" />}</div>
                  <div className="text-xs text-gray-400">Includes estimated taxes & fees</div>
                </div>

                <div className="text-right">
                  <span className="inline-flex items-center gap-2 text-xs text-gray-500">
                    <svg className="w-4 h-4 text-green-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" aria-hidden="true">
                      <path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 2v6m0 8v6M4 12h16" />
                    </svg>
                    <span>Secure payment</span>
                  </span>
                </div>

              </div>

            </div>

            <Button 
              type="submit" 
              className="w-full cursor-pointer"
              disabled={holdExpired}
            >
              {holdExpired ? "Hold Expired" : "Pay Now"}
            </Button>

          </div>
        </aside>
      </div>

    </main>
  );
}