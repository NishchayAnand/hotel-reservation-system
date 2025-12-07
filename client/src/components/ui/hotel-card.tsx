"use client"

import Image from "next/image";
import { useRouter } from "next/navigation";

import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";

import type { Hotel } from "@/types/hotel";

interface HotelCardProps {
  hotel: Hotel;
  checkInDate: string;
  checkOutDate: string;
}

export default function HotelCard({hotel, checkInDate, checkOutDate} : HotelCardProps) {

  const router = useRouter();

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

  const handleNavigate = () => {
    const q = new URLSearchParams({
      hotelId: id,
      checkInDate: checkInDate,
      checkOutDate: checkOutDate
    }).toString();
    router.push(`/hotels/hotel-details?${q}`);
  };

  return (
    <div 
      className="flex flex-col md:grid md:grid-cols-6 md:h-60 rounded-2xl border cursor-pointer overflow-hidden"
      onClick={handleNavigate}
    >
            
      {/* Thumbnail - full width on mobile, 2 cols on desktop */}
      <div id="thumbnail" className="relative w-full h-48 md:h-full md:col-span-2">
        <Image
          src={thumbnailUrl ?? "/images/jaipur/the-johri/thumbnail/photo1.jpg"}
          alt={name}
          fill={true}
          className="object-cover"
        />
      </div>

      {/* Hotel info - stacked on mobile, 3 cols on desktop */}
      <div className="flex flex-col border-r-0 md:border-r md:col-span-3 p-4 md:p-5"> 
        <h3 id="name" className="text-base md:text-lg font-semibold">{name}</h3>
        <p className="mb-3 md:mb-4 text-xs md:text-sm text-gray-500">
          {address} {rating && `| ${rating} of 5`}
        </p> 
        <Separator className="hidden md:block" />
        <div className="py-2 flex flex-wrap gap-1.5 md:gap-2">
          {amenities.slice(0, 3).map(amenity => (
            <Badge key={amenity.id} className="text-xs">{amenity.description}</Badge>
          ))}
          {amenities.length > 3 && (
            <Badge variant="outline" className="text-xs">+{amenities.length - 3} more</Badge>
          )}
        </div>
        <div className="mt-auto"></div>
        <p className="text-xs md:text-sm text-gray-600 line-clamp-2">{shortDescription}</p>
      </div>
            
      {/* Price and CTA - full width on mobile, 1 col on desktop */}
      <div className="flex flex-row md:flex-col md:col-span-1 justify-between md:justify-end items-center md:items-stretch p-4 md:p-5 border-t md:border-t-0">
        <div className="md:mb-4">
          <p className="text-lg md:text-xl font-semibold">₹{minAvgRatePerNight}</p>
          <p className="text-xs text-gray-600 whitespace-nowrap">per night + Taxes</p>
        </div>
        <Button 
          className="md:w-full cursor-pointer font-semibold text-sm md:text-base"
          onClick={handleNavigate}
        >
          View
        </Button>
      </div>
            
    </div>
  );
}