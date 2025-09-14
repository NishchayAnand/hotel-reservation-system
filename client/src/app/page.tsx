import { SearchHotelForm } from "@/components/ui/search-hotel-form";
import Image from "next/image";


export default function Home() {
  return (
    <main>

      {/* Hero Section */}
      <section className="w-full h-screen lg:px-10 lg:py-20">
        <div className="relative w-full h-full">
          {/* Hero Image */}
          <Image
            src="/images/home-page-hero-section.jpg"
            alt="Hero Image"
            fill={true} // makes the image fill the parent container
            className="object-cover lg:block"
          />
          {/* Search Hotel Form */}
          <div className="absolute left-1/2 -translate-x-1/2 bottom-1/2 translate-y-1/2 lg:w-[calc(100%-10rem)] lg:bottom-[-4rem] lg:translate-y-0">
            <SearchHotelForm />
          </div>
        </div>
      </section>

      {/*
      <section className="w-full h-screen flex flex-col pt-10 items-center">
        <h1 className="text-lg font-bold">HOUSEHOLD OF HOSPITALITY</h1><br />
        <p className="px-60 text-center">Rooted in the spirit of India’s timeless tradition of “Atithi Devo Bhava”</p><br />
        <p className="px-96 text-center">Our platform blends modern convenience with heartfelt hospitality. With us, every booking is simple, every stay is seamless, and every guest becomes part of our Gharana.</p>
      </section>
      */}

    </main>
  );
}