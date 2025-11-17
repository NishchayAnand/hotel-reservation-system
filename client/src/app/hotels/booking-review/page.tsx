"use client";

import { useSearchParams } from "next/navigation";

export default function ReviewPage() {

  const searchParams = useSearchParams();
  const hotelId = searchParams.get("hotelId") ?? "";
  const checkInDate = searchParams.get("checkInDate") ?? ""; // (ISO: YYYY-MM-DD)
  const checkOutDate = searchParams.get("checkOutDate") ?? ""; // (ISO: YYYY-MM-DD)

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
            className="p-5 border rounded-lg shadow-sm bg-white flex flex-col sm:flex-row gap-4 items-start"
          >
            <img
              src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
              alt="Hotel thumbnail"
              className="w-full sm:w-28 h-20 rounded-md object-cover flex-shrink-0"
            />

            <div className="flex-1 min-w-0">
              <div className="flex items-start justify-between gap-4">
                <div className="min-w-0">
                  <h2 className="text-lg font-semibold truncate">Rajasthan Heritage Hotel</h2>
                  <div className="mt-2 flex items-center gap-3 text-xs text-gray-500">
                    <span className="inline-flex items-center gap-2">
                      <svg className="w-4 h-4 text-gray-400" viewBox="0 0 24 24" fill="none" stroke="currentColor">
                        <path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 2l3 7h7l-5.5 4 2 7L12 16l-6.5 4 2-7L2 9h7l3-7z" />
                      </svg>
                      Fort area, Jaipur
                    </span>
                    <span className="inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium bg-yellow-50 text-yellow-800">
                      4.4 ★
                    </span>
                    <span className="inline-flex items-center text-xs text-gray-400">Free Wi‑Fi</span>
                  </div>
                </div>

                <div className="text-right flex-shrink-0">
                  <div className="text-sm text-gray-500">Dates</div>
                  <div className="text-sm my-1 font-medium">21 Oct 2025 → 22 Oct 2025</div>
                  <div className="text-xs text-gray-400 mt-1">1 night</div>
                </div>
              </div>

              <div className="mt-4 flex items-center justify-between gap-4">
                <div className="text-sm text-gray-600">
                  <div className="inline-flex items-center gap-2 text-xs bg-green-50 text-green-700 px-2 py-0.5 rounded">
                    Flexible dates
                  </div>
                  <div className="mt-2 text-xs text-gray-500">Breakfast not included</div>
                </div>

              </div>
            </div>
          </div>

          {/* Rooms Summary */}
          <div id="rooms-summary" className="p-4 border rounded-lg bg-white shadow-sm">
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