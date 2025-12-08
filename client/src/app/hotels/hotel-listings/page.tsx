"use client"

import { useSearchParams } from "next/navigation";
import { useEffect, useState, Suspense } from "react";

import HotelCard from "@/components/ui/hotel-card";
import SkeletonCard from "@/components/ui/skeleton-card";

import type { Hotel } from "@/types/hotel";

const searchAPIUrl = process.env.NEXT_PUBLIC_SEARCH_API_BASE_URL || "http://localhost:8084";

function SearchPageContent() {
  const searchParams = useSearchParams();
  const checkInDate = searchParams.get("checkInDate") ?? "";
  const checkOutDate = searchParams.get("checkOutDate") ?? "";

  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // fetch hotels from search-service
  useEffect(() => {
    const qs = searchParams.toString();

    const fetchHotels = async () => {
      setLoading(true);
      try {
        const res = await fetch(`${searchAPIUrl}/api/hotels/hotel-listing?${qs}`);
        if (!res.ok) {
          setHotels([]);
          return;
        }
        const data = await res.json();
        setHotels(Array.isArray(data) ? data : []);
      } catch (err: unknown) {
        console.error("Failed to fetch hotels", err);
        setHotels([]);
      } finally {
        setLoading(false);
      }
    };

    fetchHotels();
  }, [searchParams]);

  const renderSkeletons = (count = 3) => {
    return Array.from({ length: count }).map((_, i) => <SkeletonCard key={i} />);
  };

  return (
    <main className="w-full min-h-screen pt-20 pb-10 px-10">
      <div className="w-full flex flex-col lg:flex-row gap-4 lg:gap-6">

        {/* Filters Section - Fixed on large screens */}
        <aside
          id="filter-section"
          className="border-b lg:sticky lg:top-20 lg:w-64 lg:h-[calc(100vh-5rem)] lg:overflow-y-auto lg:border-r p-4 flex-shrink-0"
        >
          <div className="space-y-4">
            <h3 className="font-semibold text-lg">Filters</h3>
            {/* Add your filter components here */}
          </div>
        </aside>

        {/* Hotel Cards Section - Scrollable */}
        <div
          id="hotel-card-container"
          className="flex-1 min-h-0"
        >
          <div className="flex flex-col gap-4 sm:gap-5 p-2 sm:p-4">
            {loading
              ? renderSkeletons(4)
              : hotels.map((hotel) => (
                  <HotelCard
                    key={hotel.id}
                    hotel={hotel}
                    checkInDate={checkInDate}
                    checkOutDate={checkOutDate}
                  />
                ))}
          </div>
        </div>
      </div>
    </main>
  );
}

export default function SearchPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen pt-24 flex items-center justify-center">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-gray-900 mx-auto" />
            <p className="mt-4 text-gray-600">Loading hotel listings...</p>
          </div>
        </div>
      }
    >
      <SearchPageContent />
    </Suspense>
  );
}