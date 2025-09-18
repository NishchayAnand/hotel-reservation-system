import React from "react";

export default function SearchPage({
  searchParams,
}: {
  searchParams?: { destination?: string; checkInDate?: string; checkOutDate?: string };
}) {
  const destination = searchParams?.destination ?? "";
  const checkInDate = searchParams?.checkInDate ?? "";
  const checkOutDate = searchParams?.checkOutDate ?? "";

  // minimal: display params and placeholder list; replace with fetch to search-service when ready
  return (
    <main className="max-w-4xl mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4">Search results</h1>
      <p className="mb-4">
        Destination: {destination || "—"} | Check-in: {checkInDate || "—"} | Check-out: {checkOutDate || "—"}
      </p>

      <section>
        {/* replace with real fetch to /search-service or API route */}
        <div className="text-gray-600">Hotels will be listed here after you search.</div>
      </section>
    </main>
  );
}