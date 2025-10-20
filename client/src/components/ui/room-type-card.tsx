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
        <div className="h-40 grid grid-cols-7 rounded-2xl border cursor-pointer overflow-hidden">
            
            <div id="thumbnail" className="relative col-span-2 h-full">
              <Image
                src="/images/jaipur/the-johri/thumbnail/photo1.jpg"
                alt="room type image"
                fill={true}
                className="object-cover overflow-hidden"
              />
            </div>

            <div className="flex col-span-3 flex-col border-r p-5"> 
                <h3 id="name" className="text-lg font-semibold">{roomType.name}</h3>
                <Separator />
                <div className="py-2 flex flex-wrap gap-2">
                    {(roomType?.amenities ?? []).map((amenity) => (
                        <Badge key={amenity.id}>{amenity.description}</Badge>
                    ))}
                </div>  
            </div>
            
            <div className="flex flex-col col-span-2 justify-end p-5">
                <p className="text-lg font-semibold">₹{roomType.avgPricePerNight}</p>
                <p className="text-xs text-gray-600 whitespace-nowrap mb-4">per night + Taxes</p>
                <Select value={selected} onValueChange={handleValueChange}>
                    <SelectTrigger className="w-full">
                        {/* placeholder shown when selected === "" */}
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