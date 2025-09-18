import { SearchHotelForm } from "@/components/ui/search-hotel-form";
import Image from "next/image";

export default function Home() {
  return (
    <main>

      {/* Hero Section */}
      <section className="w-full h-screen lg:px-10 lg:pt-20 lg:pb-10">
        <div className="relative w-full h-[80%]">
          {/* Hero Image */}
          <Image
            src="/images/home-page-hero-section.jpg"
            alt="Hero Image"
            fill={true} // makes the image fill the parent container
            className="object-cover lg:block"
          />
          {/* Search Hotel Form */}
          <div className="absolute left-1/2 -translate-x-1/2 bottom-1/2 translate-y-1/2 lg:w-[calc(100%-30rem)] lg:bottom-[-4rem] lg:translate-y-0">
            <SearchHotelForm />
          </div>
        </div>
        <div className="w-full h-[20%] flex flex-col justify-end items-center px-50 text-center">
          {/*<h1 className="text-lg font-bold">HOUSE OF HOSPITALITY</h1><br/>*/}
          <p className="text-sm leading-relaxed text-center">Rooted in the spirit of India’s timeless tradition of “Atithi Devo Bhava”. Our platform blends modern convenience with heartfelt hospitality. With us, every booking is simple, every stay is seamless, and every guest becomes part of our Gharana.</p>
        </div>
      </section>

      <section className="w-full h-screen flex flex-col pl-10 pr-10 lg:px-10 lg:py-10">
          <h1 className="text-xl font-bold">POPULAR DESTINATIONS</h1><br/>
          <div className="flex h-full gap-4">
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/jaipur.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/kashmir.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
            <div className="relative w-1/3 h-full">
              <Image
                src="/images/goa.jpg"
                alt="Hotel 1"
                fill={true}
                className="object-cover"
              />
            </div>
          </div>
      </section>

      <section className="max-w-4xl mx-auto py-12 px-4">
          <p className="text-sm leading-relaxed text-center">
        We make every stay the very best by adding thoughtful touches and proactive care — from handpicked, vetted rooms and bespoke recommendations to handling special requests and offering 24/7 local support. Our team anticipates needs, verifies details, and stays involved so every guest enjoys a seamless, memorable stay.
          </p>
      </section>

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

    </main>
  );
}