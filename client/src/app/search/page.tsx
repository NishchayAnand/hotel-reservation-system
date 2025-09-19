"use client"

import { SearchHotelForm2 } from "@/components/ui/search-page-form";
import { useSearchParams } from "next/navigation";

export default function SearchPage() {
  const searchParams = useSearchParams();

  const destination = searchParams.get("destination");
  const checkInDate = searchParams.get("checkInDate");
  const checkOutDate = searchParams.get("checkOutDate");

  return (
    <main>

      <section className="w-full h-screen bg-amber-200">
        <div className="text-gray-600 bg-amber-800">Hotels will be listed here after you search.</div>
      </section>

    </main>
  );
}