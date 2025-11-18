"use client";

import { Reservation } from "@/types/reservation";
import { Hotel } from "@/types/hotel";
import { MapPinIcon } from "@heroicons/react/24/solid";

import { useSearchParams } from "next/navigation";
import { useState, useEffect, useMemo } from "react";
import { Skeleton } from "@/components/ui/skeleton";

export default function ReviewPage() {

  const searchParams = useSearchParams();
  const reservationId = searchParams.get("reservationId") ?? "";

  const [reservation, setReservation] = useState<Reservation | null>(null);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  // hotel details after reservation is loaded
  const [hotel, setHotel] = useState<Hotel | null>(null);
  const [hotelLoading, setHotelLoading] = useState<boolean>(false);
  const [hotelError, setHotelError] = useState<string | null>(null);

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

  // compute number of nights 
  const nights = useMemo(() => {
    if (!reservation) return;
    const checkIn = new Date(reservation.checkInDate);
    const checkOut = new Date(reservation.checkOutDate);
    const ms = checkOut.getTime() - checkIn.getTime();
    return Math.round(ms / (1000 * 60 * 60 * 24));
  }, [reservation?.checkInDate, reservation?.checkOutDate]);

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

      <header id="title" className="mb-6">
        <h1 className="text-3xl font-semibold">Booking Review</h1>
        <p className="text-sm text-gray-500 mt-1">Confirm your selection and proceed to payment</p>
      </header>

      <div id="main-content" className="grid grid-cols-3 gap-6">

        <section id="hero-section" className="col-span-2 space-y-4">

          {/* Booking Summary */}
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

                {/* Hotel Details */}
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
              <li className="py-3 flex justify-between items-center">
                <div>
                  <div className="font-medium">Deluxe Room</div>
                  <div className="text-xs text-gray-500">1 room • Non-refundable</div>
                </div>
                <div className="text-right">
                  <div className="font-semibold">₹3,000</div>
                  <div className="text-xs text-gray-400">per night</div>
                </div>
              </li>

              <li className="py-3 flex justify-between items-center">
                <div>
                  <div className="font-medium">Executive Suite</div>
                  <div className="text-xs text-gray-500">0 rooms selected</div>
                </div>
                <div className="text-right">
                  <div className="font-semibold">₹6,000</div>
                  <div className="text-xs text-gray-400">per night</div>
                </div>
              </li>
            </ul>
          </div>

          {/* Guest Details */}
          <div id="guest-details" className="p-4 border rounded-lg bg-white shadow-sm">
            <div className="flex items-start justify-between">
              <div>
                <h3 className="text-md font-medium mb-1">Guest details</h3>
                <p className="text-xs text-gray-500">Primary guest information — used for check-in and confirmation</p>
              </div>
              <div>
                <button
                  className="text-sm text-black hover:underline"
                  aria-label="Edit guest details"
                >
                  Edit
                </button>
              </div>
            </div>

            <div className="mt-4 grid grid-cols-2 gap-y-3 gap-x-6 text-sm text-gray-700">
              <div className="flex flex-col">
                <span className="text-xs text-gray-500">Name</span>
                <span className="font-medium">John Doe</span>
              </div>

              <div className="flex flex-col">
                <span className="text-xs text-gray-500">Email</span>
                <span className="font-medium">john.doe@example.com</span>
              </div>

              <div className="flex flex-col">
                <span className="text-xs text-gray-500">Phone</span>
                <span className="font-medium">+91 98765 43210</span>
              </div>

              <div className="flex flex-col">
                <span className="text-xs text-gray-500">Guests</span>
                <span className="font-medium">2 adults, 1 child</span>
              </div>
            </div>

            <div className="mt-4 text-xs text-gray-400">
              Note: Valid ID will be required at check-in. You can update guest details later if needed.
            </div>
          </div>

        </section>

        <aside id="side-section" className="col-span-1">
          <div className="p-4 border rounded-lg bg-white shadow-sm sticky top-24">
            <h3 className="text-lg font-semibold mb-2">Payment summary</h3>

            <div className="text-sm text-gray-600 mb-4">
              <div className="flex justify-between">
                <span>Subtotal</span>
                <span className="font-medium">₹3,000</span>
              </div>
              <div className="flex justify-between mt-2">
                <span>Taxes (est.)</span>
                <span className="font-medium">₹540</span>
              </div>
              <div className="flex justify-between mt-2">
                <span>Service fee</span>
                <span className="font-medium">₹100</span>
              </div>
            </div>

            <div className="border-t pt-3 mb-4" role="region" aria-label="Price summary">
              <div className="flex justify-between items-start gap-4">
              <div>
                <div className="text-sm text-gray-500">Total (incl. taxes & fees)</div>
                <div className="text-xl my-2 font-semibold text-gray-900">₹3,640</div>
                <div className="text-xs text-gray-400">Includes estimated taxes & fees</div>
              </div>

              <div className="text-right">
                <span className="inline-flex items-center gap-2 text-xs text-gray-500">
                <svg className="w-4 h-4 text-green-600" viewBox="0 0 24 24" fill="none" stroke="currentColor" aria-hidden="true">
                  <path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 2v6m0 8v6M4 12h16" />
                </svg>
                <span className="sr-only">Secure payment</span>
                <span>Secure payment</span>
                </span>
              </div>
              </div>
              <div aria-live="polite" className="sr-only">Total payable is ₹3,640</div>
            </div>

            <button className="w-full inline-flex items-center justify-center gap-2 py-2 px-3 bg-black text-white rounded-md shadow-sm">
              Proceed to payment
            </button>

          </div>
        </aside>
      </div>

    </main>
  );
}