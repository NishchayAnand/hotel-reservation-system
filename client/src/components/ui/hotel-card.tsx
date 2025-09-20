
"use client"

import Image from "next/image";
import Link from "next/link";

import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";
import {Button} from "@/components/ui/button";

import type { Hotel } from "@/types/hotel";

export default function HotelCard(props: Hotel) {
    return (
        <div className="h-60 grid grid-cols-6 rounded-2xl border cursor-pointer overflow-hidden">
            
            <div id="thumbnail" className="relative col-span-2 h-full">
              <Image
                src={props.thumbnailUrl}
                alt={props.name}
                fill={true}
                className="object-cover overflow-hidden"
              />
            </div>

            <div className="flex col-span-3 flex-col border-r p-5"> 
                <h3 id="name" className="text-lg font-semibold">{props.name}</h3>
                <p className="mb-4 text-sm text-gray-500">{props.address} | {props.rating} of 5</p>
                <Separator />
                <div className="py-2 flex gap-2">
                    {props.amenties.map(amentiy => <Badge key={amentiy}>{amentiy}</Badge>)}
                </div>
                <div className="mt-auto"></div>
                <p className="text-sm text-gray-600">
                    {props.description}
                </p>
            </div>
            
            <div className="flex flex-col col-span-1 justify-end p-5">
              <p className="text-lg font-semibold">₹{props.avgPricePerNight}</p>
              <p className="text-xs text-gray-600 whitespace-nowrap mb-4">for 2 night + Taxes</p>
              <Link href={`/hotels/${props.id}`}>
                <Button className="w-full cursor-pointer font-semibold">
                  View
                </Button>
              </Link>
            </div>
            
          </div>
    );
}