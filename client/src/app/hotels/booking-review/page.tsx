"use client";

export default function ReviewPage() {
  return (
    <main className="mt-16 max-w-6xl mx-auto p-6">

      <header id="title" className="mb-6">
        <h1 className="text-3xl font-semibold">Booking Review</h1>
        <p className="text-sm text-gray-500 mt-1">Confirm your selection and proceed to payment</p>
      </header>

      <div id="main-content" className="grid grid-cols-3 gap-6">

        <section id="hero-section" className="col-span-2 space-y-4">

          {/* Booking Summary */}
          <div id="booking-summary" className="flex gap-4 p-4 border rounded-lg shadow-sm bg-white">
            <img
              src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
              alt="Hotel thumbnail"
              className="w-28 h-20 rounded-md object-cover"
            />
            <div className="flex-1">
              <div className="flex items-start justify-between">
                <div>
                  <h2 className="text-lg font-medium">Rajasthan Heritage Hotel</h2>
                  <p className="text-xs text-gray-500 mt-1">Fort area, Jaipur • 4.4 ⭐</p>
                </div>
                <div className="text-right">
                  <div className="text-sm text-gray-500">Dates</div>
                  <div className="font-medium">21 Oct 2025 → 22 Oct 2025</div>
                  <div className="text-xs text-gray-400">1 night</div>
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
                  className="text-sm text-indigo-600 hover:underline"
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

            <div className="border-t pt-3 mb-4">
              <div className="flex justify-between items-center">
                <div>
                  <div className="text-sm text-gray-500">Total</div>
                  <div className="text-xl font-semibold">₹3,640</div>
                  <div className="text-xs text-gray-400">Includes estimated taxes & fees</div>
                </div>
              </div>
            </div>

            <button className="w-full inline-flex items-center justify-center gap-2 py-2 px-3 bg-black text-white rounded-md shadow-sm">
              Proceed to payment
            </button>

            <p className="text-xs text-gray-400 mt-3">
              This is a static preview. Payment integrations and confirmation flows will be enabled in a later iteration.
            </p>
          </div>
        </aside>
      </div>

    </main>
  );
}