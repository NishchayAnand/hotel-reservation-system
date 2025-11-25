"use client";

import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { Reservation } from "@/types/reservation";

export default function ConfirmationPage() {

  const searchParams = useSearchParams();
  const reservationId = searchParams.get("reservationId") ?? "";

  const [reservation, setReservation] = useState<Reservation | null>(null);
  const [loading, setLoading] = useState<boolean>(!!reservationId);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!reservationId) return;
    const abort = new AbortController();
    const fetchReservation = async () => {
      setLoading(true);
      setError(null);
      try {
        const baseUrl = process.env.NEXT_PUBLIC_RESERVATION_SERVICE_URL || "http://localhost:8084";
        const res = await fetch(`${baseUrl}/api/reservations/${reservationId}`, {
          signal: abort.signal,
          headers: { Accept: "application/json" },
        });

        if (!res.ok) {
          const body = await res.json().catch(() => null);
          throw new Error(body?.message ?? `Failed to fetch reservation (${res.status})`);
        }
        const data = await res.json();
        setReservation({ ...data });
      } catch (err: any) {
        if (err.name !== "AbortError") setError(err.message ?? "Failed to load reservation");
      } finally {
        setLoading(false);
      }
    };
    fetchReservation();
    return () => abort.abort();
  }, [reservationId]);

  const fmt = useMemo(() => {
    const currency = reservation?.currency ?? "INR";
    return new Intl.NumberFormat("en-IN", { style: "currency", currency, minimumFractionDigits: 0 });
  }, [reservation?.currency]);

  const formatDate = (d?: string) => {
    if (!d) return "—";
    const dt = new Date(d);
    if (Number.isNaN(dt.getTime())) return d;
    return dt.toLocaleDateString(undefined, { day: "2-digit", month: "short", year: "numeric" });
  };

  return (
    <main className="w-full min-h-screen flex flex-col pt-24 px-6">
      <div className="bg-white max-w-4xl mx-auto border rounded-lg overflow-hidden">
        <div className="p-6">
          <h1 className="text-3xl font-semibold">Booking Confirmed</h1>
          <p className="text-sm text-gray-500 mt-1">Thank you — your payment was successful.</p>
        </div>

        <div className="p-6 grid grid-cols-1 md:grid-cols-3 gap-6">
          <section className="md:col-span-2 space-y-4">
            <div className="rounded-lg p-4 border bg-green-50">
              {loading ? (
                <div className="text-sm text-gray-600">Loading reservation…</div>
              ) : error ? (
                <div className="text-sm text-red-600">{error}</div>
              ) : !reservation ? (
                <div className="text-sm text-gray-600">Reservation not found.</div>
              ) : (
                <>
                  <div className="flex items-start justify-between">
                    <div>Reservation ID: {reservation?.id ?? "—"}</div>
                    <div className="text-right text-sm text-gray-600">
                      <div>{formatDate(reservation.checkInDate)} → {formatDate(reservation.checkOutDate)}</div>
                      <div className="mt-1">{reservation.status ?? "CONFIRMED"}</div>
                    </div>
                  </div>
                </>
              )}
            </div>

            <div className="p-4 border rounded-lg bg-white">
              <h3 className="text-sm font-medium mb-2">Need help?</h3>
              <p className="text-sm text-gray-600">If you have questions about your booking, contact support at <a href="mailto:support@example.com" className="text-blue-600">support@example.com</a> or call +91 12345 67890.</p>
            </div>
          </section>

          <aside className="space-y-4">
            <div className="p-4 border rounded-lg bg-white">
              <div className="text-xs text-gray-500">Total paid</div>
              <div className="text-2xl font-semibold mt-1">{reservation?.amount ? fmt.format(reservation.amount) : "—"}</div>
              <div className="text-xs text-gray-400 mt-2">Includes taxes & fees (estimated)</div>
            </div>
          </aside>
        </div>
      </div>

      <footer className="max-w-4xl mx-auto mt-6 text-center text-xs text-gray-400">
        <div className="mt-1">If you need to modify your booking, visit your bookings page or contact support.</div>
      </footer>
    </main>
  );
}