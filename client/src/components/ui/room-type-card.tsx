
import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";

export default function RoomTypeCard() {
    return (
        <div className="h-40 grid grid-cols-6 rounded-2xl border cursor-pointer overflow-hidden">
            
            <div id="thumbnail" className="relative col-span-2 h-full">
              <Image
                src="/images/jaipur/the-johri/thumbnail.jpg"
                alt="room type image"
                fill={true}
                className="object-cover overflow-hidden"
              />
            </div>

            <div className="flex col-span-3 flex-col border-r p-5"> 
                <h3 id="name" className="text-lg font-semibold">Deluxe Room</h3>
                <Separator />
                <div className="py-2 flex gap-2">
                     <Badge className="px-2">Wifi</Badge>
                        <Badge>Gym</Badge>
                        <Badge>Swimming Pool</Badge>
                        <Badge>Spa</Badge>
                        <Badge>Restaurant</Badge>
                </div>
            </div>
            
            <div className="flex flex-col col-span-1 justify-end p-5">
                <p className="text-lg font-semibold">₹4500</p>
                <p className="text-xs text-gray-600 whitespace-nowrap mb-4">for 2 night + Taxes</p>
            </div>
            
          </div>
    );
}