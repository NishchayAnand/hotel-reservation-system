
import Image from "next/image";
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

    const handleSelect = (qty: number) => {
        onQuantityChange(roomType.id, qty);
    };

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
                <Select>
                    <SelectTrigger className="w-full">
                        <SelectValue placeholder={selectedQty} />
                    </SelectTrigger>
                    <SelectContent>
                        {Array.from({ length: roomType.availableRoomCount }, (_, i) => i + 1)
                            .map(n => (
                                <SelectItem key={n} value={String(n)} onChange={() => handleSelect(n)}>
                                    {n}
                                </SelectItem>
                            ))
                        }
                    </SelectContent>
                </Select>
            </div>
            
          </div>
    );
}