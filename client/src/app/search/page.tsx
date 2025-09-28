"use client"

import { useSearchParams } from "next/navigation";

import HotelCard from "@/components/ui/hotel-card";

import type { Hotel } from "@/types/hotel";
import { useEffect, useState } from "react";

// typed placeholder data — replace with fetched data later
{/*
const hotels: Hotel[] = [
    {
      id: "101",
      name: "The Johri",
      address: "Jaipur, Rajasthan",
      description: "A luxury seaside resort with modern amenities.",
      thumbnailUrl: "/images/jaipur/the-johri/thumbnail.jpg",
      rating: 4.6,
      avgPricePerNight: 4500.0,
      amenties: ["Free WiFi", "Swimming Pool", "Spa"]
    },
    {
      id: "103",
      name: "Jaipur House",
      address: "Jaipur, Rajasthan",
      description: "Charming hill-side hotel with valley views.",
      thumbnailUrl: "/images/jaipur/jaipur-house/thumbnail.jpg",
      rating: 4.4,
      avgPricePerNight: 2000.0,
      amenties: ["Free Parking", "Breakfast Included", "Mountain View"]
    }
];
*/}


export default function SearchPage() {
  const searchParams = useSearchParams();

  const destination = searchParams.get("destination");
  // const checkInDate = searchParams.get("checkInDate");
  // const checkOutDate = searchParams.get("checkOutDate");

  const [hotels, setHotels] = useState<Hotel[]>([]);

  // fetch hotels from search-service
  useEffect(() => {
    const qs = searchParams.toString();
    fetch(`http://localhost:8080/search/hotels?${qs}`)
      .then((res) => res.json())
      .then((data) => setHotels(data))
  }, [searchParams]);
  
  return (
    <main>

      <section className="w-full pt-20 pb-10 px-10">
        
        <div className="grid grid-cols-4 gap-6">
          {/* Filters Section */}
          <div className="col-span-1 border-r">
            
          </div>

          {/* Hotel Cards Section */}
          <div className="col-span-3">
            <h1 className="text-lg font-semibold my-5">Hotels near {destination}</h1>
            <div className="flex flex-col gap-5">
              {hotels.map(hotel => <HotelCard key={hotel.id} {...hotel} />)}
            </div>
            
          </div>

        </div>     
      </section>

      {/*
      
      <div className="p-4 border rounded-lg shadow">
              <h2 className="text-md font-semibold mb-4">Filters</h2>
              <div>
                <label className="block text-sm font-medium mb-2">Price Range</label>
                <input type="range" className="w-full" />
              </div>
              <div className="mt-4">
                <label className="block text-sm font-medium mb-2">Rating</label>
                <select className="w-full border rounded p-2">
                  <option value="any">Any</option>
                  <option value="4">4+ Stars</option>
                  <option value="3">3+ Stars</option>
                </select>
              </div>
            </div>
      
      */}

    </main>
  );
}