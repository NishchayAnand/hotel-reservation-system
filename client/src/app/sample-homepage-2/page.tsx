
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { SearchIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Badge } from "@/components/ui/badge";

import { SearchHotelForm } from "@/components/ui/search-hotel-form";
import Image from "next/image";

interface Location {
    id: number;
    city: string;
}

export default async function Home() {

  // server-side fetch
  const res = await fetch(`${process.env.NEXT_PUBLIC_HOTEL_API_BASE_URL || "http://localhost:8081"}/api/locations`);
  const locations = res.ok ? await res.json() : [];

  const destinations = Array.isArray(locations)
    ? locations.map((loc: Location) => ({
        label: loc.city,
        value: String(loc.id)
    }))
    : [];

  return (
    <main className="px-10 mb-10">

      {/* Hero Section */}
      <section className="w-full h-130 pt-20 pb-10">

        <h1 className="text-5xl my-10">Find your perfect stay</h1>

        <div className="relative w-full h-full lg:h-[90%]">

          <Image
            src="/images/sample-home-page-3.jpg"
            alt="Hero Image"
            fill={true}
            className="object-cover rounded-2xl overflow-hidden"
          />

          {/* Hotel search form for mobile and tablet screens */}
          <div className="lg:hidden absolute w-full max-w-xl left-1/2 -translate-x-1/2 px-4 cursor-pointer">
            <Popover>
              <PopoverTrigger asChild>
                <div className="flex justify-between items-center bg-white rounded-4xl m-3 p-3 ">
                  <p className="text-gray-400 text-md ml-3">Search Hotels Here...</p>
                  <Button className="rounded-4xl cursor-pointer">
                    <SearchIcon/>
                  </Button>
                </div>
              </PopoverTrigger>
              <PopoverContent className="z-200">
                <SearchHotelForm destinations={destinations} />
              </PopoverContent>
            </Popover>
          </div> 

          {/* Search Hotel Form for large screens */}
          <div className="hidden lg:block absolute lg:left-1/2 lg:-translate-x-1/2 lg:bottom-[-4rem] lg:translate-y-0 lg:w-[calc(100%-10rem)] z-100">
            <Card>
              <CardContent>
                <SearchHotelForm destinations={destinations} />
              </CardContent>
            </Card>
          </div>

        </div>

      </section>

      <section className="w-full mt-50">

        <h1 className="text-3xl">Popular Accomodations</h1>

        <div className="flex justify-center gap-4 mt-10">

            <div className="cursor-pointer">
                <Card className="relative w-80 h-70 shadow-none overflow-hidden">
                    <Image
                        src="/images/photo-sample-1.jpg"
                        alt="Jaipur"
                        fill={true}
                        className="object-cover"
                    />
                </Card>
                <div className="flex justify-between">
                    <h3 className="text-sm font-semibold mt-2">Fat Plate</h3>
                    <div className="flex items-center gap-1">
                        <span className="text-xs">★</span>
                        <span className="text-xs font-medium">4.5</span>
                    </div>
                </div>
                <p className="text-xs text-gray-500 font-semibold mt-1">₹5,000/night</p>       
            </div>

            <div className="cursor-pointer">
                <Card className="relative w-80 h-70 shadow-none overflow-hidden">
                    <Image
                        src="/images/photo-sample-2.jpg"
                        alt="Jaipur"
                        fill={true}
                        className="object-cover"
                    />
                </Card>
                <div className="flex justify-between">
                    <h3 className="text-sm font-semibold mt-2">Elysian Stays</h3>
                    <div className="flex items-center gap-1">
                        <span className="text-xs">★</span>
                        <span className="text-xs font-medium">4.5</span>
                    </div>
                </div>
                <p className="text-xs text-gray-500 font-semibold mt-1">₹8,000/night</p>       
            </div>

            <div className="cursor-pointer">
                <Card className="relative w-80 h-70 shadow-none overflow-hidden">
                    <Image
                        src="/images/photo-sample-3.jpg"
                        alt="Jaipur"
                        fill={true}
                        className="object-cover"
                    />
                </Card>
                <div className="flex justify-between">
                    <h3 className="text-sm font-semibold mt-2">Mohru Estate</h3>
                    <div className="flex items-center gap-1">
                        <span className="text-xs">★</span>
                        <span className="text-xs font-medium">4.5</span>
                    </div>
                </div>
                <p className="text-xs text-gray-500 font-semibold mt-1">₹7,500/night</p>       
            </div>

            <div className="cursor-pointer">
                <Card className="relative w-80 h-70 shadow-none overflow-hidden">
                    <Image
                        src="/images/photo-sample-4.jpg"
                        alt="Jaipur"
                        fill={true}
                        className="object-cover"
                    />
                </Card>
                <div className="flex justify-between">
                    <h3 className="text-sm font-semibold mt-2">Johnson Studio</h3>
                    <div className="flex items-center gap-1">
                        <span className="text-xs">★</span>
                        <span className="text-xs font-medium">4.5</span>
                    </div>
                </div>
                <p className="text-xs text-gray-500 font-semibold mt-1">₹5,000/night</p>       
            </div>

        </div>

      </section>

      <section className="mt-20 w-full h-100 rounded-2xl bg-black/90 flex flex-col justify-center items-center gap-8 p-30">

        <div className="text-center">
          <h2 className="text-3xl font-bold text-white mb-4">Special Offers For You</h2>
          <p className="text-md text-white/90 leading-relaxed max-w-3xl mx-auto">
            Unlock exclusive deals and limited-time offers on premium stays. From early bird discounts to seasonal packages, 
            we bring you the best rates on handpicked accommodations that promise comfort, luxury, and unforgettable experiences.
          </p>
        </div>

        <Button className="w-50 border border-white">Explore Now</Button>
      </section>

    </main>
  );
}

