"use client"

import Image from "next/image";
import { useState, useEffect } from "react";
import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";

import { RoomType } from "@/types/roomType";

type RoomTypeCardProps = {
    roomType: RoomType;
    selectedQty: number;
    onQuantityChange: (roomTypeId: string, qty: number) => void;
};

export default function RoomTypeCard({roomType, selectedQty, onQuantityChange} : RoomTypeCardProps) {
    // keep controlled selection state (string because Select works with string values)
    const [selected, setSelected] = useState<string>(selectedQty > 0 ? String(selectedQty) : "");

    useEffect(() => {
        // keep local state in sync when parent selectedQty changes
        setSelected(selectedQty > 0 ? String(selectedQty) : "");
    }, [selectedQty]);

    const handleValueChange = (val: string) => {
        setSelected(val);
        const qty = Number(val);
        onQuantityChange(roomType.id, qty);
    };

    const max = Math.max(0, roomType.availableRoomCount ?? 0);
    const options = Array.from({ length: max }, (_, i) => i + 1);

    return (
        <div className="flex flex-col md:grid md:grid-cols-7 md:h-40 rounded-2xl border cursor-pointer overflow-hidden">
            
            {/* Thumbnail - full width on mobile, 2 cols on desktop */}
            <div id="thumbnail" className="relative w-full h-48 md:h-full md:col-span-2">
              <Image
                src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
                alt="room type image"
                fill={true}
                className="object-cover"
              />
            </div>

            {/* Room info - stacked on mobile, 3 cols on desktop */}
            <div className="flex flex-col border-r-0 md:border-r md:col-span-3 p-4 md:p-5"> 
                <h3 id="name" className="text-base md:text-lg font-semibold">{roomType.name}</h3>
                <Separator className="hidden md:block" />
                <div className="py-2 flex flex-wrap gap-1.5 md:gap-2">
                    {(roomType?.amenities ?? []).slice(0, 4).map((amenity) => (
                        <Badge key={amenity.id} className="text-xs">{amenity.description}</Badge>
                    ))}
                    {(roomType?.amenities ?? []).length > 4 && (
                        <Badge variant="outline" className="text-xs">+{roomType.amenities.length - 4}</Badge>
                    )}
                </div>  
            </div>
            
            {/* Price and selector - horizontal on mobile, vertical on desktop */}
            <div className="flex flex-row md:flex-col md:col-span-2 justify-between md:justify-end items-center md:items-stretch p-4 md:p-5 border-t md:border-t-0">
                <div className="md:mb-4">
                    <p className="text-lg md:text-xl font-semibold">₹{roomType.avgPricePerNight}</p>
                    <p className="text-xs text-gray-600 whitespace-nowrap">per night + Taxes</p>
                </div>
                <Select value={selected} onValueChange={handleValueChange}>
                    <SelectTrigger className="w-20 md:w-full">
                        <SelectValue placeholder="0" />
                    </SelectTrigger>
                    <SelectContent>
                        {options.map(n => (
                            <SelectItem key={n} value={String(n)}>
                                {n}
                            </SelectItem>
                        ))}
                    </SelectContent>
                </Select>
            </div>
            
        </div>
    );
}