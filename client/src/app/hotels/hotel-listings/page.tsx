"use client"

import { useSearchParams } from "next/navigation";

import HotelCard from "@/components/ui/hotel-card";
import SkeletonCard from "@/components/ui/skeleton-card";

import type { Hotel } from "@/types/hotel";
import { useEffect, useState } from "react";

const searchAPIUrl = process.env.NEXT_PUBLIC_SEARCH_API_BASE_URL  || "http://localhost:8084" ;


export default function SearchPage() {

  const searchParams = useSearchParams();
  const checkInDate = searchParams.get("checkInDate") ?? ""; // (ISO: YYYY-MM-DD)
  const checkOutDate = searchParams.get("checkOutDate") ?? ""; // (ISO: YYYY-MM-DD)

  const [hotels, setHotels] = useState<Hotel[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  
  // fetch hotels from search-service
  useEffect(() => {
    
    const qs = searchParams.toString();

    console.log(searchAPIUrl);
    const fetchHotels = async () => {
      setLoading(true);
      try {
        const res = await fetch(`${searchAPIUrl}/api/hotels/hotel-listing?${qs}`);
        if(!res.ok) {
          setHotels([]);
          return;
        }
        const data = await res.json();
        console.log(data);
        setHotels(Array.isArray(data) ? data : []);
      } catch(err: any) {
        console.error("Failed to fetch hotels", err);
        setHotels([]);
      } finally {
        setLoading(false);
      }
    };
    
    fetchHotels();

  }, []);

  const renderSkeletons = (count = 3) => {
    return Array.from({ length : count }).map((_, i) => (
      <SkeletonCard key={i}/>
    ));
  };
  
  return (
    <main className="w-full h-screen pt-20 pb-10 px-10">
        
      {/* 
        Use a fixed inner height so only the right column scrolls.
        pt-20 (5rem) + pb-10 (2.5rem) are accounted for below:
        available height = 100vh - 7.5rem
      */}
      <div className="w-full h-[calc(100vh-7.5rem)] grid grid-cols-4 gap-6">
        
        {/* Filters Section - remains visible (does not scroll) */}
        <div id="filter-section" className="col-span-1 border-r h-full">
          {/* put filters here; this column stays fixed while the right column scrolls */}
        </div>

        {/* Hotel Cards Section - scrolls within available viewport height */}
        <div id="hotel-card-container" className="col-span-3 h-full overflow-y-auto hide-scrollbar">
          <div className="flex flex-col gap-5 p-4">
            {loading ? renderSkeletons(4) : hotels.map(hotel => (
              <HotelCard 
                key={hotel.id} 
                hotel={hotel}
                checkInDate={checkInDate}
                checkOutDate={checkOutDate} />))}
          </div>
        </div>

      </div>     

    </main>
  );
}