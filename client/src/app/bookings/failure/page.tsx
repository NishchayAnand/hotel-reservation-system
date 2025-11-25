"use client";

import Link from "next/link";
import { useSearchParams } from "next/navigation";

export default function FailurePage() {
  const params = useSearchParams();
  const reservationId = params?.get("reservationId") ?? "";
  const reason = params?.get("reason") ?? params?.get("message") ?? "";

  return (
    <main className="min-h-screen flex items-start justify-center p-6">
      <div className="max-w-3xl w-full mt-24 bg-white shadow rounded-lg overflow-hidden">
        <div className="p-6 border-b">
          <h1 className="text-2xl font-semibold text-red-700">Booking Failed</h1>
          <p className="text-sm text-gray-500 mt-1">We couldn't complete your booking. Please try again or contact support.</p>
        </div>

        <div className="p-6 space-y-4">
          <div className="p-4 rounded bg-red-50 text-red-700">
            <div className="font-medium">Error</div>
            <div className="text-sm mt-1 text-gray-700">{reason || "An unexpected error occurred while processing your booking."}</div>
          </div>

          {reservationId && (
            <div className="text-sm text-gray-600">
              <div>Reservation ID: <span className="font-medium">{reservationId}</span></div>
              <div className="mt-1 text-xs text-gray-500">If this reservation was created, you can retry payment from the booking review page.</div>
            </div>
          )}

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <Link
              href={reservationId ? `/hotels/booking-review?reservationId=${encodeURIComponent(reservationId)}` : `/hotels`}
              className="inline-flex items-center justify-center gap-2 px-4 py-2 bg-black text-white rounded text-sm hover:opacity-90"
            >
              Retry / Review booking
            </Link>

            <Link
              href="/bookings"
              className="inline-flex items-center justify-center gap-2 px-4 py-2 border rounded text-sm hover:bg-gray-50"
            >
              View my bookings
            </Link>
          </div>

          <div className="text-sm text-gray-600">
            <p>If you need help, contact support:</p>
            <p className="mt-1"><a href="mailto:support@example.com" className="text-blue-600">support@example.com</a> · <a href="tel:+911234567890" className="text-blue-600">+91 12345 67890</a></p>
          </div>

          <div className="text-xs text-gray-400">If payment was charged but the booking failed, please contact support with the reservation ID shown above.</div>
        </div>
      </div>
    </main>
  );
}