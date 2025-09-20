
import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel"
import Image from "next/image";

import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import RoomTypeCard from "@/components/ui/room-type-card";

export default function HotelPage() {
    return (
        <main>

            <section className="w-full h-screen p-20">
                <Carousel>
                    <CarouselContent>
                        <CarouselItem>
                            <div id="carousel-image" className="relative aspect-video rounded-2xl overflow-hidden">
                                <Image
                                    src="/images/jaipur/the-johri/thumbnail.jpg"
                                    alt="image"
                                    fill={true}
                                    className="object-cover"
                                />
                            </div>
                        </CarouselItem>
                    </CarouselContent>
                    <CarouselPrevious />
                    <CarouselNext />
                </Carousel>
            </section>

            <section className="w-full grid grid-cols-3 p-10">
                <div className="col-span-2 border-r pr-10">
                    <h1 className="text-2xl font-semibold">The Johri</h1>
                    <p className="text-md my-4 font-semibold">Jaipur, Rajasthan</p>
                    <Separator />
                    <div className="flex gap-2 my-5">
                        <Badge className="px-2">Wifi</Badge>
                        <Badge>Gym</Badge>
                        <Badge>Swimming Pool</Badge>
                        <Badge>Spa</Badge>
                        <Badge>Restaurant</Badge>
                        <Badge>Bar</Badge>
                        <Badge>Parking</Badge>
                        <Badge>Pet Friendly</Badge>
                        <Badge>Air Conditioning</Badge>
                        <Badge>Room Service</Badge>
                    </div>
                    <Separator />
                    <p className="mt-10 text-gray-600">
                        Nestled in the heart of Jaipur, The Johri offers a luxurious stay with world-class amenities and exceptional service. 
                        Experience the rich culture and heritage of Rajasthan while enjoying modern comforts in a serene environment.
                    </p>
                    <br />
                    <p className="text-gray-600 mb-10">
                        The hotel features elegantly designed rooms, each equipped with state-of-the-art facilities to ensure a comfortable and memorable stay. 
                        Guests can indulge in a variety of culinary delights at the on-site restaurant, unwind at the spa, or take a refreshing dip in the swimming pool. 
                        Whether you're visiting for leisure or business, The Johri promises an unparalleled experience that blends tradition with modernity.
                    </p>
                    <Separator className="mb-5"/>
                    <div className="flex flex-col gap-4">
                        <h2 className="text-md font-semibold">Select Rooms</h2>
                        <RoomTypeCard />
                        <RoomTypeCard />
                    </div>
                </div>
                <div className="col-span-1"></div>
            </section>

        </main>
    );
}