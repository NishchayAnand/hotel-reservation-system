
import Image from "next/image";
import {Separator} from "@/components/ui/separator";
import {Badge} from "@/components/ui/badge";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";

export default function RoomTypeCard() {
    return (
        <div className="h-40 grid grid-cols-7 rounded-2xl border cursor-pointer overflow-hidden">
            
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
                <div className="py-2 flex flex-wrap gap-2">
                    <Badge className="px-2">Breakfast Included</Badge>
                    <Badge>Minibar</Badge>
                    <Badge>Study Room</Badge>
                    <Badge>Kettel</Badge>
                    <Badge>Water Heater</Badge>
                </div>  
            </div>
            
            <div className="flex flex-col col-span-2 justify-end p-5">
                <p className="text-lg font-semibold">₹4500</p>
                <p className="text-xs text-gray-600 whitespace-nowrap mb-4">for 2 night + Taxes</p>
                <Select>
                    <SelectTrigger className="w-full">
                        <SelectValue placeholder="For 1 Room" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="1">1</SelectItem>
                        <SelectItem value="2">2</SelectItem>
                        <SelectItem value="3">3</SelectItem>
                        <SelectItem value="4">4</SelectItem>
                    </SelectContent>
                </Select>
            </div>
            
          </div>
    );
}