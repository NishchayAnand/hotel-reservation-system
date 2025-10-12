
import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel"
import Image from "next/image";

import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import RoomTypeCard from "@/components/ui/room-type-card";
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "@/components/ui/accordion"
import { Button } from "@/components/ui/button"
import {
  Card,
  CardContent,
  CardFooter,
} from "@/components/ui/card"
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

import { Calendar } from "@/components/ui/calendar";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover";
import { Check, ChevronDownIcon } from "lucide-react";

// static image list for the carousel
const carouselImages = [
    "/images/jaipur/the-johri/carousel/photo1.jpg",
    "/images/jaipur/the-johri/carousel/photo2.jpg"
];

export default function HotelPage() {
    return (
        <main>
            
            {/* Hero Section - Image Carousel */}
            <section id="photo-gallery" className="w-full h-screen p-20">
                <Carousel>
                    <CarouselContent>
                        {carouselImages.map((src, idx) => (
                            <CarouselItem key={idx}>
                                <div id="carousel-image" className="relative aspect-video rounded-2xl overflow-hidden">
                                    <Image
                                        src={src}
                                        alt={`hotel photo ${idx + 1}`}
                                        fill={true}
                                        className="object-cover"
                                    />
                                </div>
                            </CarouselItem>
                        ))}
                    </CarouselContent>
                    <CarouselPrevious />
                    <CarouselNext />
                </Carousel>
            </section>
            
            {/* Content Section */}
            <section id="grid-container" className="w-full grid grid-cols-3 p-10">
                
                {/* Main Content */}
                <div id="main-content" className="col-span-2 pr-10">
                    
                    {/* Hotel Details */}
                    <article id="hotel-details-container" className="bg-amber-300">
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
                    </article>
                    
                    
                    <Separator className="mb-5"/>
                    
                    {/* Room Type Details */}
                    <article id="room-type-details-container" className="flex flex-col gap-4">
                        <h2 className="text-md font-semibold">Select Rooms</h2>
                        <RoomTypeCard />
                        <RoomTypeCard />
                    </article>

                    <Separator className="mt-10 mb-5"/>

                    <article id="faqs-container"className="flex flex-col gap-4">
                        <h2 className="text-md font-semibold">FAQs</h2>
                        <Accordion
                            type="single"
                            collapsible
                            className="w-full"
                            defaultValue="item-1"
                        >

                            <AccordionItem value="item-1">
                                <AccordionTrigger>Cancellation Policy</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4 text-balance">
                                    <p>
                                        Our flagship product combines cutting-edge technology with sleek
                                        design. Built with premium materials, it offers unparalleled
                                        performance and reliability.
                                    </p>
                                    <p>
                                        Key features include advanced processing capabilities, and an
                                        intuitive user interface designed for both beginners and experts.
                                    </p>
                                </AccordionContent>
                            </AccordionItem>

                            <AccordionItem value="item-2">
                                <AccordionTrigger>Shipping Details</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4 text-balance">
                                    <p>
                                        We offer worldwide shipping through trusted courier partners.
                                        Standard delivery takes 3-5 business days, while express shipping
                                        ensures delivery within 1-2 business days.
                                    </p>
                                    <p>
                                        All orders are carefully packaged and fully insured. Track your
                                        shipment in real-time through our dedicated tracking portal.
                                    </p>
                                </AccordionContent>
                            </AccordionItem>

                            <AccordionItem value="item-3">
                                <AccordionTrigger>Return Policy</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4 text-balance">
                                    <p>
                                        We stand behind our products with a comprehensive 30-day return
                                        policy. If you&apos;re not completely satisfied, simply return the
                                        item in its original condition.
                                    </p>
                                    <p>
                                        Our hassle-free return process includes free return shipping and
                                        full refunds processed within 48 hours of receiving the returned
                                        item.
                                    </p>
                                </AccordionContent>
                            </AccordionItem>

                        </Accordion>

                    </article>

                </div>
                
                <div className="col-span-1 px-5">

                    <Card className="w-full">
                        <CardContent>
                            <div className="flex flex-col gap-6">

                                <div className="p-4 bg-gray-50 rounded-lg">
                                    <h3 className="text-sm font-semibold text-gray-800">Booking Summary</h3>
                                    <div className="text-sm text-gray-900 mt-2 space-y-1">
                                        <div className="flex justify-between">
                                            <span>2 Deluxe Room</span>
                                            <span>₹4500 x 2 = ₹9000</span>
                                        </div>
                                        <div className="flex justify-between">
                                            <span>1 Suite</span>
                                            <span>₹6000 x 1 = ₹6000</span>
                                        </div>
                                        <div className="border-t border-gray-300 mt-2 pt-2 flex justify-between font-semibold">
                                            <span>Total</span>
                                            <span>₹15,000</span>
                                        </div>
                                        <p className="text-xs text-gray-600 mt-1 text-right">for 2 nights + Taxes</p>
                                    </div>
                                </div>
                            </div>
                        </CardContent>
                        <CardFooter className="flex-col gap-2">
                            <Button type="submit" className="w-full">
                                Book Now
                            </Button>
                        </CardFooter>
                    </Card>

                </div>
            </section>

        </main>
    );
}