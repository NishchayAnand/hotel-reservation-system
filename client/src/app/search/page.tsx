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

      <section className="w-full pt-20 pb-10 px-10">
        
        <div className="grid grid-cols-4 gap-6">
          {/* Filters Section */}
          <div className="col-span-1 border-r">
            
          </div>

          {/* Hotel Cards Section */}
          <div className="col-span-3">
            <h1 className="text-lg font-semibold my-5">Stays near {destination}</h1>
            {destination === "Jaipur" && (
              <p className="text-sm text-gray-600 mb-10">
                Jaipur, also known as the Pink City, is a vibrant destination known for its rich history, stunning architecture, and warm hospitality. Explore the majestic forts, colorful bazaars, and exquisite cuisine during your stay.
              </p>
            )}
            {/* Render multiple HotelCard components */}
            <div className="flex flex-col gap-5">
              <HotelCard />
              <HotelCard />
              <HotelCard />
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