
"use client"

import Image from "next/image";
import Link from "next/link";

import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";

import type { Hotel } from "@/types/hotel";

export default function HotelCard(hotel: Hotel) {

  const {
    id,
    name = "Untitled Hotel",
    address = "Address not available",
    shortDescription = "No description available",
    thumbnailUrl,
    rating,
    amenities = [],
    minAvgRatePerNight
  } = hotel as Hotel;

  return (
    <div className="h-60 grid grid-cols-6 rounded-2xl border cursor-pointer overflow-hidden">
            
      <div id="thumbnail" className="relative col-span-2 h-full">
        <Image
          src={thumbnailUrl ?? "/images/jaipur/the-johri/thumbnail/photo1.jpg"}
          alt={name}
          fill={true}
          className="object-cover overflow-hidden"
        />
      </div>

      <div className="flex col-span-3 flex-col border-r p-5"> 
        <h3 id="name" className="text-lg font-semibold">{name}</h3>
        <p className="mb-4 text-sm text-gray-500">{address} | {rating} of 5</p> 
        <Separator />
        <div className="py-2 flex flex-wrap gap-2">
          {amenities.map(amentiy => <Badge key={amentiy.id}>{amentiy.description}</Badge>)}
        </div>
        <div className="mt-auto"></div>
        <p className="text-sm text-gray-600">{shortDescription}</p>
      </div>
            
      <div className="flex flex-col col-span-1 justify-end p-5">
        <p className="text-lg font-semibold">₹{minAvgRatePerNight}</p>
        <p className="text-xs text-gray-600 whitespace-nowrap mb-4">per night + Taxes</p>
        <Link href={`/hotel-details/${id}`}>
          <Button className="w-full cursor-pointer font-semibold">View</Button>
        </Link>
      </div>
            
    </div>
    );
}