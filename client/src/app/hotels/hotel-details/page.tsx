"use client"

import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel"
import Image from "next/image";
import { useSearchParams } from "next/navigation";

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

import { Hotel } from "@/types/hotel";
import { RoomType } from "@/types/roomType";
import { useState, useEffect } from "react";
import SkeletonCard from "@/components/ui/skeleton-card";
import HotelDetailsSkeleton from "@/components/ui/hotel-details-skeleton";

// static image list for the carousel
const DEFAULT_CAROUSEL = [
    "/images/jaipur/the-johri/carousel/photo1.jpg",
    "/images/jaipur/the-johri/carousel/photo2.jpg"
];

export default function HotelPage() {

    const searchParams = useSearchParams();
    const hotelId = searchParams.get("hotelId") ?? "";
    const checkInDate = searchParams.get("checkInDate") ?? ""; // (ISO: YYYY-MM-DD)
    const checkOutDate = searchParams.get("checkOutDate") ?? ""; // (ISO: YYYY-MM-DD)

    const [hotel, setHotel] = useState<Hotel | null>(null);
    const [roomTypes, setRoomTypes] = useState<RoomType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        let isMounted = true; // 👈 flag to track if component is still mounted
        const fetchData = async () => {
            setLoading(true);
            try {
                const [hotelResponse, roomTypeResponse] = await Promise.all([
                    fetch(`http://localhost:8081/api/hotels/${hotelId}`),
                    fetch(`http://localhost:8080/api/hotels/hotel-details/${hotelId}/room-types`
                        + `?checkInDate=${checkInDate}&checkOutDate=${checkOutDate}`)
                ]);

                if (!isMounted) return;

                if (hotelResponse.ok) {
                    const h = await hotelResponse.json();
                    setHotel(h);
                } else {
                    console.error("Failed to fetch hotel details", hotelResponse.status);
                    setHotel(null);
                }

                if (roomTypeResponse.ok) {
                    const rts = await roomTypeResponse.json();
                    setRoomTypes(Array.isArray(rts) ? rts : []);
                } else {
                    console.error("Failed to fetch room types", roomTypeResponse.status);
                    setRoomTypes([]);
                }
            } catch (err) {
                console.error("Error fetching hotel or room types", err);
                if(isMounted) {
                    setHotel(null);
                    setRoomTypes([]);
                }

            } finally {
                if(isMounted) setLoading(false);
            }
        };

        fetchData();

        return () => { isMounted = false; };
        
    }, []);

    const renderSkeletons = (count = 3) => {
        return Array.from({ length : count }).map((_, i) => (
          <SkeletonCard key={i}/>
        ));
    };

    return (
        <main>
            
            {/* Hero Section - Image Carousel */}
            <section id="photo-gallery" className="w-full h-screen p-20">
                <Carousel>
                    <CarouselContent>
                        {DEFAULT_CAROUSEL.map((src, idx) => (
                            <CarouselItem key={idx}>
                                <div id="carousel-image" className="relative aspect-video rounded-2xl overflow-hidden">
                                    <Image
                                        src={src}
                                        alt={`${name} photo ${idx + 1}`}
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
                    {loading ? (
                        <HotelDetailsSkeleton />
                    ) : (
                        <article id="hotel-details">
                            <h1 className="text-2xl font-semibold">{hotel?.name}</h1>
                            <p className="text-md my-4 font-semibold">{hotel?.address}</p>
                            <Separator />
                            <div className="flex flex-wrap gap-2 my-5">
                                {(hotel?.amenities ?? []).map((amenity) => (
                                    <Badge key={amenity.id}>{amenity.description}</Badge>
                                ))}
                            </div>
                            <Separator />
                            <p className="mt-10 text-gray-600">
                            {hotel?.shortDescription}
                            </p>
                            <br />
                            <p className="text-gray-600 mb-10">
                                {hotel?.longDescription}
                            </p>
                        </article>
                    )}
                               
                    <Separator className="mb-5"/>
                    
                    {/* Room Type Details */}
                    <article id="room-type-details" className="flex flex-col gap-4">
                        <h2 className="text-md font-semibold mb-4">Select Rooms</h2>
                        {loading ? renderSkeletons(2) : (
                            roomTypes.map(roomType => (
                                <RoomTypeCard key={roomType.id} {...roomType} />
                            ))
                        )}   
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

                            <AccordionItem value="item-2">
                                <AccordionTrigger>Rules and Regulations</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4 text-balance">
                                    <ul className="list-disc pl-5">
                                        <li>
                                            Guests are required to present a valid government-issued ID at the time of check-in. 
                                            The primary guest must be at least 18 years old.
                                        </li>
                                        <li>
                                            Smoking is strictly prohibited in all indoor areas. A designated smoking area is available outside the premises.
                                        </li>
                                        <li>
                                            Pets are not allowed on the property. Service animals are permitted with prior notice.
                                        </li>
                                        <li>
                                            Please respect the quiet hours between 10 PM and 7 AM to ensure a comfortable stay for all guests.
                                        </li>
                                    </ul>
                                </AccordionContent>
                            </AccordionItem>

                            <AccordionItem value="item-1">
                                <AccordionTrigger>Cancellation Policy</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4">
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

                            <AccordionItem value="item-3">
                                <AccordionTrigger>Refund Policy</AccordionTrigger>
                                <AccordionContent className="flex flex-col gap-4">
                                    <p>
                                        We are committed to ensuring customer satisfaction with a transparent refund policy. 
                                        If you are eligible for a refund, the amount will be processed back to your original payment method.
                                    </p>
                                    <p>
                                        Refunds are typically processed within 7-10 business days after the request is approved. 
                                        Please note that certain conditions may apply, and processing times may vary depending on your payment provider.
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