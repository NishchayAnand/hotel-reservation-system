"use client"

import {Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious} from "@/components/ui/carousel"
import Image from "next/image";
import { useSearchParams } from "next/navigation";

import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import RoomTypeCard from "@/components/ui/room-type-card";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

import { Hotel } from "@/types/hotel";
import { RoomType } from "@/types/roomType";
import { useState, useEffect, useMemo } from "react";
import SkeletonCard from "@/components/ui/skeleton-card";
import HotelDetailsSkeleton from "@/components/ui/hotel-details-skeleton";
import HotelFaqs from "@/components/ui/hotel-faqs";

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

    // selections map: roomTypeId -> quantity
    const [selections, setSelections] = useState<Record<string, number>>({});

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

    const handleQuantityChange = (roomTypeId: string, qty: number) => {
        setSelections(prev => {
            const copy = {... prev};
            copy[roomTypeId] = qty;
            return copy;
        });
    };

    const renderSkeletons = (count = 3) => {
        return Array.from({ length : count }).map((_, i) => (
          <SkeletonCard key={i}/>
        ));
    };

    // compute number of nights from checkOutDate - checkInDate
    const nights = useMemo(() => {
        const checkIn = new Date(checkInDate);
        const checkOut = new Date(checkOutDate);
        const ms = checkOut.getTime() - checkIn.getTime();
        return Math.round(ms / (1000 * 60 * 60 * 24));
    }, [checkInDate, checkOutDate]);

    // breakdown selection pricing and total
    const breakdown = useMemo(() => {
        const items: {id: string, name: string, qty: number, rate: number, subtotal: number}[] = [];
        let total = 0;
        for(const [id, qty] of Object.entries(selections)) {
            const rt = roomTypes.find(roomType => roomType.id === id);
            if (!rt) continue;
            const rate = rt.avgPricePerNight ?? 0;
            const subtotal = qty * rate * nights;
            items.push({id, name: rt.name, qty, rate, subtotal});
            total += subtotal;
        }
        return { items, total };
    }, [selections, roomTypes, nights]);

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
                                <RoomTypeCard 
                                    key={roomType.id} 
                                    roomType={roomType}
                                    selectedQty={selections[roomType.id] ?? 0}
                                    onQuantityChange={handleQuantityChange}
                                 />
                            ))
                        )}   
                    </article>

                    <Separator className="mt-10 mb-5"/>
                    
                    {/* Hotel FAQs */}
                    <HotelFaqs />
                    
                </div>
                
                {/* Booking Summary - pricing breakdown */}
                <aside id="booking-summary"className="col-span-1">
                    <Card className="w-full shadow-none">
                        <CardContent>
                            <div className="flex flex-col p-4 bg-gray-50 rounded-lg">
                                <h3 className="text-md font-semibold mb-5">Booking Summary</h3>
                                {breakdown.items.length === 0 ? (
                                    <p className="text-sm">No rooms selected</p>
                                ) : (
                                    <div>
                                        <div id="pricing-breakdown" className="text-sm">
                                            {breakdown.items.map(it => (
                                                <div key={it.id} className="flex justify-between">
                                                    <div>
                                                        <div>{it.name}</div>
                                                        <div>₹{it.rate} x {it.qty} x {nights} nights</div>
                                                    </div>
                                                    <div>₹{it.subtotal}</div>
                                                </div>
                                            ))}
                                            <div >
                                                <span>2 Deluxe Room</span>
                                                <span>₹4500 x 2 = ₹9000</span>
                                            </div>
                                            <div className="flex justify-between">
                                                <span>1 Suite</span>
                                                <span>₹6000 x 1 = ₹6000</span>
                                            </div>
                                        </div>
                                        <div id="total-cost">
                                            <div className="border-t border-gray-300 mt-2 pt-2 flex justify-between font-semibold">
                                                <span>Total</span>
                                                <span>₹15,000</span>
                                            </div>
                                            <p className="text-xs text-gray-600 mt-1 text-right">for 2 nights + Taxes</p>
                                        </div>
                                    </div>
                                )}
                            </div>
                        </CardContent>
                        <CardFooter>
                            <Button type="submit" className="w-full">Book Now</Button>
                        </CardFooter>
                    </Card>
                </aside>

            </section>

        </main>
    );
}