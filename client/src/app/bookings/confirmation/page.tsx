"use client";

import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";
import { Reservation } from "@/types/reservation";
import { CheckCircleIcon, ClockIcon, CalendarDaysIcon, CurrencyRupeeIcon } from "@heroicons/react/24/outline";

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
    <main className="min-h-screen flex flex-col items-center pt-24 pb-16 px-4">
      <div className="max-w-4xl w-full space-y-6">
        <header className="text-center">
          <div className="inline-flex items-center gap-3 rounded-full bg-green-100 text-green-700 px-4 py-1 text-sm font-medium">
            <CheckCircleIcon className="w-5 h-5" />
            Booking confirmed
          </div>
          <h1 className="mt-4 text-3xl font-semibold text-gray-900">Your stay is all set!</h1>
          <p className="mt-2 text-gray-500">
            Thank you for your payment. We’ve locked in your reservation and sent the confirmation to your email.
          </p>
        </header>

        <section className="bg-white border border-gray-200 rounded-2xl overflow-hidden">
          <div className="p-6">
            {loading ? (
              <div className="text-sm text-gray-600">Loading reservation…</div>
            ) : error ? (
              <div className="text-sm text-red-600">{error}</div>
            ) : !reservation ? (
              <div className="text-sm text-gray-600">Reservation not found.</div>
            ) : (
              <>
                <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
                  <div>
                    <div className="text-xs uppercase tracking-wide text-gray-400">Reservation</div>
                    <div className="mt-1 flex items-center gap-2 text-gray-900">
                      <span className="font-mono text-lg font-semibold">#{reservation.id}</span>
                      <span className="inline-flex items-center gap-1 rounded-full bg-green-50 px-2.5 py-0.5 text-xs font-medium text-green-700">
                        {reservation.status ?? "CONFIRMED"}
                      </span>
                    </div>
                  </div>

                  <div className="bg-slate-50 border border-slate-200 rounded-lg px-4 py-3 text-sm text-gray-600">
                    <div className="flex items-center gap-2">
                      <ClockIcon className="w-4 h-4 text-gray-500" />
                      <span>Check-in pending — we’ll remind you 24 hours prior.</span>
                    </div>
                  </div>
                </div>

                <dl className="mt-6 grid grid-cols-1 gap-4 sm:grid-cols-2">
                  <div className="rounded-lg border border-gray-200 bg-white p-4">
                    <div className="flex items-center gap-2 text-sm font-medium text-gray-500">
                      <CalendarDaysIcon className="w-4 h-4" />
                      Check-in
                    </div>
                    <div className="mt-2 text-lg font-semibold text-gray-900">{formatDate(reservation.checkInDate)}</div>
                  </div>
                  <div className="rounded-lg border border-gray-200 bg-white p-4">
                    <div className="flex items-center gap-2 text-sm font-medium text-gray-500">
                      <CalendarDaysIcon className="w-4 h-4" />
                      Check-out
                    </div>
                    <div className="mt-2 text-lg font-semibold text-gray-900">{formatDate(reservation.checkOutDate)}</div>
                  </div>
                  <div className="rounded-lg border border-gray-200 bg-white p-4">
                    <div className="flex items-center gap-2 text-sm font-medium text-gray-500">
                      Guest
                    </div>
                    <div className="mt-2 text-base font-medium text-gray-900">—</div>
                    <div className="text-sm text-gray-500">—</div>
                  </div>
                  <div className="rounded-lg border border-gray-200 bg-white p-4">
                    <div className="flex items-center gap-2 text-sm font-medium text-gray-500">
                      <CurrencyRupeeIcon className="w-4 h-4" />
                      Total paid
                    </div>
                    <div className="mt-2 text-2xl font-semibold text-gray-900">
                      {reservation?.amount ? fmt.format(reservation.amount) : "—"}
                    </div>
                    <div className="text-sm text-gray-400">Taxes & fees included</div>
                  </div>
                </dl>

                {/* {reservation.reservedItems?.length ? (
                  <div className="mt-6">
                    <h2 className="text-sm font-semibold text-gray-700 uppercase tracking-wide">Room summary</h2>
                    <ul className="mt-3 divide-y divide-gray-100 rounded-lg border border-gray-200 bg-white">
                      {reservation.reservedItems.map((item, index) => (
                        <li key={index} className="flex items-center justify-between px-4 py-3 text-sm">
                          <div>
                            <div className="font-medium text-gray-900">{item.name ?? `Room ${item.id ?? ""}`}</div>
                            <div className="text-gray-500">{item.quantity ?? item.quantity ?? 0} room(s)</div>
                          </div>
                          <div className="font-semibold text-gray-900">
                            {fmt.format(item.rate ?? 0)}
                          </div>
                        </li>
                      ))}
                    </ul>
                  </div>
                ) : null} */}
              </>
            )}
          </div>

          <footer className="bg-slate-50 px-6 py-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between border-t border-slate-200">
            <div className="text-sm text-gray-500">
              Keep this page or download the confirmation for your check-in desk.
            </div>
            <div className="flex flex-col sm:flex-row gap-2">
              <button
                onClick={() => window.print()}
                className="inline-flex rounded-lg border border-gray-300 px-4 py-2 text-sm font-medium text-gray-700 hover:bg-white"
              >
                Download confirmation
              </button>
              <Link
                href="/bookings"
                className="inline-flex text-center rounded-lg bg-black px-4 py-2 text-sm font-medium text-white hover:opacity-90"
              >
                View my bookings
              </Link>
            </div>
          </footer>
        </section>

        <section className="text-center text-sm text-gray-500">
          Need help? Email <a href="mailto:support@example.com" className="text-blue-600">support@example.com</a> or call +91 12345 67890.
        </section>
      </div>
    </main>
  );
}