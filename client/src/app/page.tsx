import { SearchHotelForm } from "@/components/ui/search-hotel-form";
import Image from "next/image";
import { Card } from "@/components/ui/card";

export default function Home() {
  return (
    <main>

      {/* Hero Section */}
      <section className="w-full h-screen lg:px-10 lg:pt-20 lg:pb-10">
        
        <div className="relative w-full h-[90%]">
          {/* Hero Image */}
          <Image
            src="/images/home-page-hero-section.jpg"
            alt="Hero Image"
            fill={true} // makes the image fill the parent container
            className="object-cover lg:block rounded-2xl overflow-hidden"
          />
          {/* Search Hotel Form */}
          <div className="absolute left-1/2 -translate-x-1/2 bottom-1/2 translate-y-1/2 lg:w-[calc(100%-30rem)] lg:bottom-[-4rem] lg:translate-y-0">
            <SearchHotelForm />
          </div>
        </div>

      </section>

      {/* Popular Destination Section */}
      <section className="w-full h-screen flex flex-col p-10">

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
          With us, every booking is simple, every stay is seamless, and every guest becomes part of our Gharana.
        </p>
      </section>

      {/*

        Rooted in the spirit of India’s timeless tradition of “Atithi Devo Bhava”. Our platform blends modern convenience with heartfelt hospitality. 

              <Image
                src="/images/kashmir.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
              <Image
                src="/images/goa.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />

      <section className="w-full h-screen flex flex-col pl-10 pr-10 lg:px-10 lg:py-10">
          <h1 className="text-xl font-bold">EXPERIENCES</h1><br/>
          <div className="flex h-full gap-4">
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/kayaking.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/cycling.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/picnic.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
          </div>
      </section>

      */}

    </main>
  );
}