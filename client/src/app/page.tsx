
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { SearchIcon } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"

import { SearchHotelForm } from "@/components/ui/search-hotel-form";
import Image from "next/image";

interface Location {
    id: number;
    city: string;
}

export default async function Home() {

  // server-side fetch
  const res = await fetch("http:localhost:8081/api/locations");
  const locations = res.ok ? await res.json() : [];
  console.log(locations);

  const destinations = Array.isArray(locations)
    ? locations.map((loc: Location) => ({
        label: loc.city,
        value: String(loc.id)
    }))
    : [];

  return (
    <main className="px-10">

      {/* Hero Section */}
      <section className="w-full h-screen pt-20 pb-10">

        <div className="relative w-full h-full lg:h-[90%]">

          <Image
            src="/images/home-page-hero-section.jpg"
            alt="Hero Image"
            fill={true}
            className="object-cover rounded-2xl overflow-hidden"
          />

          {/* Hotel search form for mobile and tablet screens */}
          <div className="lg:hidden absolute bottom-0 w-full max-w-xl left-1/2 -translate-x-1/2 px-4 cursor-pointer">
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
          <div className="hidden lg:block absolute lg:left-1/2 lg:-translate-x-1/2 lg:bottom-[-4rem] lg:translate-y-0 lg:w-[calc(100%-10rem)]">
            <Card>
              <CardHeader>
                <CardTitle>Search Hotels</CardTitle>
              </CardHeader>
              <CardContent>
                <SearchHotelForm destinations={destinations} />
              </CardContent>
            </Card>
          </div>

        </div>

      </section>

      {/* Popular Destination Section */}
      <section className="w-full h-screen flex flex-col px-20 py-10">

          {/* Header */}
          <h1 className="text-xl font-bold">POPULAR DESTINATIONS</h1><br/>

          {/* Destination Card Container */}
          <div className="w-full flex flex-grow gap-4">

            {/* Jaipur */}
            <div id="destination-card" className="flex-1 rounded-2xl overflow-hidden">
              <div id="image-card-holder" className="relative w-full h-full group">
                <div id="overlay" className="absolute inset-0 z-10 flex flex-col justify-center items-center bg-black text-white opacity-0 transition-opacity duration-300 ease-in-out group-hover:opacity-70">
                  <p className="text-md font-bold">JAIPUR</p>
                  <p className="text-sm mt-2 px-4 text-center">
                    Known as the Pink City, Jaipur combines majestic palaces, colorful bazaars, and rich Rajasthani culture—making it a top choice for history lovers, shoppers, and food enthusiasts.
                  </p>
                </div>
                <Image
                  src="/images/jaipur.jpg"
                  alt="Jaipur"
                  fill={true}
                  className="object-cover"
                />
              </div>
            </div>

            {/* Kashmir */}
            <div id="destination-card" className="flex-1 rounded-2xl overflow-hidden">
              <div id="image-card-holder" className="relative w-full h-full group">
                <div id="overlay" className="absolute inset-0 z-10 flex flex-col justify-center items-center bg-black text-white opacity-0 transition-opacity duration-300 ease-in-out group-hover:opacity-70">
                  <p className="text-md font-bold">KASHMIR</p>
                  <p className="text-sm mt-2 px-4 text-center">
                    With its majestic Himalayas, shimmering lakes, and vibrant valleys, Kashmir offers a timeless blend of natural beauty and cultural richness that draws travelers from around the world.
                  </p>
                </div>
                <Image
                  src="/images/kashmir.jpg"
                  alt="Kashmir"
                  fill={true}
                  className="object-cover"
                />
              </div>
            </div>

            {/* Goa */}
            <div id="destination-card" className="flex-1 rounded-2xl overflow-hidden">
              <div id="image-card-holder" className="relative w-full h-full group">
                <div id="overlay" className="absolute inset-0 z-10 flex flex-col justify-center items-center bg-black text-white opacity-0 transition-opacity duration-300 ease-in-out group-hover:opacity-70">
                  <p className="text-md font-bold">GOA</p>
                  <p className="text-sm mt-2 px-4 text-center">
                    Framed by golden beaches, swaying palms, and a vibrant mix of Portuguese heritage and lively nightlife, Goa is a coastal escape that blends relaxation with celebration.
                  </p>
                </div>
                <Image
                  src="/images/goa.jpg"
                  alt="Goa"
                  fill={true}
                  className="object-cover"
                />
              </div>
            </div>

          </div>

          <div className="w-full h-[20%] flex flex-col justify-center items-center px-50 text-center">
            <p className="text-sm leading-relaxed text-center">
              We make every stay the very best by adding thoughtful touches and proactive care — from handpicked, vetted rooms and 
              bespoke recommendations to handling special requests and offering 24/7 local support. Our team anticipates needs, verifies details, 
              and stays involved so every guest enjoys a seamless, memorable stay.
            </p>
          </div>
          
      </section>

      <section className="w-full h-screen px-20 flex flex-col">
        <video controls className="w-full h-full flex flex-grow items-center justify-center rounded-2xl">
          <source src="/videos/home-page-video.mp4"/>
        </video>
        <p className="text-sm leading-relaxed text-center py-10">
          With us, every booking is simple, every stay is seamless, and every guest becomes part of our haven.
        </p>
      </section>

    </main>
  );
}