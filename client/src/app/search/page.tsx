"use client"

import { useSearchParams } from "next/navigation";

import HotelCard from "@/components/ui/hotel-card";

export default function SearchPage() {
  const searchParams = useSearchParams();

  const destination = searchParams.get("destination");
  const checkInDate = searchParams.get("checkInDate");
  const checkOutDate = searchParams.get("checkOutDate");

  return (
    <main>

      <section className="w-full p-20 px-10">
        <h1 className="text-lg font-semibold my-5">Stays near {destination}</h1>
        <HotelCard />      
      </section>

    </main>
  );
}