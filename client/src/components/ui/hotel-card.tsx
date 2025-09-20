
"use client"

import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";

export default function HotelCard() {
    return (
        <div className="h-60 flex rounded-2xl border cursor-pointer overflow-hidden">
            
            <div id="thumbnail" className="relative w-1/2 h-full">
              <Image
                src="/images/johri-jaipur.jpg"
                alt="thumbnail-image"
                fill={true}
                className="object-cover overflow-hidden"
              />
            </div>

            <div className="flex grow-1 flex-col border-r p-5"> 
                <h3 id="name" className="text-lg font-semibold">The Johri</h3>
                <p className="mb-4 text-sm text-gray-500">Jaipur, Rajasthan | 4.6 of 5</p>
                <Separator />
                <div className="py-2 flex gap-2">
                    <Badge>Pool</Badge>
                    <Badge>Wifi</Badge>
                    <Badge>Spa</Badge>
                    <Badge>Parking</Badge>
                    <Badge>Restaurant</Badge>
                    <Badge>Gym</Badge>
                </div>
                <div className="mt-auto"></div>
                <p className="text-sm text-gray-600">
                    Experience luxury and comfort at The Johri, a boutique hotel offering world-class amenities and exceptional service in the heart of Jaipur.
                </p>
            </div>
            
            <div className="flex flex-col w-1/4 justify-end p-5">
              <p className="text-lg font-semibold">₹4500</p>
            <p className="text-xs text-gray-600 whitespace-nowrap">for 2 night + Taxes</p>
            </div>
            
          </div>
    );
}