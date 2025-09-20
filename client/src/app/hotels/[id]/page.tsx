
import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel"
import Image from "next/image";

import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";

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

            <section className="w-full h-screen grid grid-cols-3 p-10">
                <div className="col-span-2 border-r pr-10">
                    <h1 className="text-2xl font-semibold">The Johri</h1>
                    <p className="text-md my-2 font-semibold">Jaipur, Rajasthan</p>
                    <Separator />
                    <div className="flex gap-2 mt-5">
                        <Badge className="px-2">Wifi</Badge>
                        <Badge>Gym</Badge>
                        <Badge>Swimming Pool</Badge>
                        <Badge>Spa</Badge>
                    </div>
                </div>
                <div className="col-span-1"></div>
            </section>

        </main>
    );
}