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
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

import { Hotel } from "@/types/hotel";
import { RoomType } from "@/types/roomType";
import { useState, useEffect, useMemo } from "react";
import SkeletonCard from "@/components/ui/skeleton-card";
import HotelDetailsSkeleton from "@/components/ui/hotel-details-skeleton";
import HotelFaqs from "@/components/ui/hotel-faqs";
import { useRouter } from "next/navigation";

// static image list for the carousel
const DEFAULT_CAROUSEL = [
    "/images/jaipur/the-johri/carousel/photo1.jpg",
    "/images/jaipur/the-johri/carousel/photo2.jpg"
];

const hotelAPIUrl = process.env.NEXT_PUBLIC_HOTEL_API_BASE_URL;
const searchAPIUrl = process.env.NEXT_PUBLIC_SEARCH_API_BASE_URL;
const reservationAPIUrl = process.env.NEXT_PUBLIC_RESERVATION_API_BASE_URL;

export default function HotelPage() {

    const router = useRouter();

    const searchParams = useSearchParams();
    const hotelId = searchParams.get("hotelId") ?? "";
    const checkInDate = searchParams.get("checkInDate") ?? ""; // (ISO: YYYY-MM-DD)
    const checkOutDate = searchParams.get("checkOutDate") ?? ""; // (ISO: YYYY-MM-DD)

    const [hotel, setHotel] = useState<Hotel | null>(null);
    const [roomTypes, setRoomTypes] = useState<RoomType[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    const [submitting, setSubmitting] = useState<boolean>(false);
    const [submitError, setSubmitError] = useState<string | null>(null);

    // selections map: roomTypeId -> quantity
    const [selections, setSelections] = useState<Record<string, number>>({});

    useEffect(() => {
        let isMounted = true; // 👈 flag to track if component is still mounted
        const fetchData = async () => {
            setLoading(true);
            try {
                const [hotelResponse, roomTypeResponse] = await Promise.all([
                    fetch(`${hotelAPIUrl || "http://localhost:8081"}/api/hotels/${hotelId}`),
                    fetch(`${searchAPIUrl || "http://localhost:8080"}/api/hotels/hotel-details/${hotelId}/room-types`
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
        const selectedItems: {id: string, name: string, qty: number, rate: number, subtotal: number}[] = [];
        let total = 0;
        for(const [id, qty] of Object.entries(selections)) {
            const rt = roomTypes.find(roomType => String(roomType.id) === id);
            if (!rt) continue;
            const rate = rt.avgPricePerNight ?? 0;
            const subtotal = qty * rate * nights;
            selectedItems.push({id, name: rt.name, qty, rate, subtotal});
            total += subtotal;
        }
        return { selectedItems, total };
    }, [selections, roomTypes, nights]);

    // helper for formatting currency
    const formatCurrency = (v: number) => new Intl.NumberFormat("en-IN", { style: "currency", currency: "INR", maximumFractionDigits: 0 }).format(v);
    
    // taxes & fees (adjust percentages/thresholds as you need)
    // const taxes = Math.round(breakdown.total * 0.18); // 18% GST example
    // const serviceFee = breakdown.total > 0 ? Math.max(50, Math.round(breakdown.total * 0.02)) : 0; // min ₹50 or 2%
    // const grandTotal = breakdown.total + taxes + serviceFee;

    const handleCreateReservation = async () => {
        setSubmitError(null);
        setSubmitting(true);
        try {
            // build reservation items from precomputed breakdown.selectedItems
            const reservationItems = breakdown.selectedItems.map( item => ({
                roomTypeId: Number(item.id),
                name: item.name,
                quantity: item.qty,
                rate: item.rate
            }));

            // idempotency key
            const requestId = crypto.randomUUID();

            const payload = {
                hotelId: Number(hotelId),
                checkInDate,
                checkOutDate,
                reservationItems,
                amount: breakdown.total,
                currency: "INR"
            };

            const baseUrl = reservationAPIUrl || "http://localhost:8084";
            const res = await fetch(`${baseUrl}/api/reservations`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "X-Request-Id": requestId
                },
                body: JSON.stringify(payload)
            });

            if (!res.ok) {
                const errBody = await res.json().catch(() => null);
                throw new Error(errBody?.message || `Reservation create failed: ${res.status}`);
            }

            const created = await res.json();

            const paramString = new URLSearchParams({ reservationId: created.reservationId }).toString();
            router.push(`/bookings/review?${paramString}`);

        } catch (err) {
            console.error("create reservation failed", err);
            setSubmitError(err instanceof Error ? err.message : "Failed to create reservation");
        } finally {
            setSubmitting(false);
        }
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
                
                {/* Booking Summary - improved UI */}
                <aside id="booking-summary" className="col-span-1">
                  <div className="sticky top-24">
                    <Card className="w-full shadow-none">
                        <CardHeader>
                            <CardTitle className="text-lg font-semibold">Booking Summary</CardTitle>
                            <CardDescription>
                                <div className="text-xs text-gray-500">
                                    {nights > 0 ? `${nights} night${nights > 1 ? "s" : ""}` : "Dates"}
                                    {" • "}
                                    {checkInDate || "—"} → {checkOutDate || "—"}
                                </div>
                            </CardDescription>
                        </CardHeader>
                      <CardContent>
                        <div className="flex flex-col gap-4">
                          {breakdown.selectedItems.length === 0 ? (
                            <div className="p-4 bg-gray-50 rounded-md text-sm text-gray-600">
                              No rooms selected
                            </div>
                          ) : (
                            <div className="space-y-3">
                                <div id="pricing-breakdown" className="text-sm">
                                    {breakdown.selectedItems.map(it => (
                                    <div key={it.id} className="flex items-center justify-between mb-2">
                                        <div>
                                        <div className="font-medium">{it.name}</div>
                                        <div className="text-xs text-gray-500">
                                            {it.qty} x {formatCurrency(it.rate)} per night
                                        </div>
                                        </div>
                                        <div className="text-right">
                                        <div className="font-semibold">{formatCurrency(it.subtotal)}</div>
                                        </div>
                                    </div>
                                    ))}
                                </div>

                                <div className="border-t border-gray-200 pt-3 text-sm space-y-2">
                                    {/*
                                    <div className="flex justify-between text-gray-600">
                                        <span>Subtotal</span>
                                        <span>{formatCurrency(breakdown.total)}</span>
                                    </div>
                                    <div className="flex justify-between text-gray-600">
                                        <span>Taxes (est.)</span>
                                        <span>{formatCurrency(taxes)}</span>
                                    </div>
                                    <div className="flex justify-between text-gray-600">
                                        <span>Service fee</span>
                                        <span>{formatCurrency(serviceFee)}</span>
                                    </div>
                                    */}
                                    <div className="flex justify-between font-semibold text-gray-900">
                                        <span>Total</span>
                                        <span>{formatCurrency(breakdown.total)}</span>
                                    </div>
                                    <div className="text-xs text-gray-500">Prices shown exclude taxes and fees.</div>
                                </div>
                            </div>
                          )}
                        </div>
                      </CardContent>
                      <CardFooter className="flex-col gap-3">
                        <Button
                          type="submit"
                          className="w-full cursor-pointer"
                          disabled={breakdown.selectedItems.length === 0 || submitting}
                          onClick={handleCreateReservation}
                        >
                          {submitting ? "Processing..." : (breakdown.selectedItems.length === 0 ? "Select rooms to continue" : `Book • ${formatCurrency(breakdown.total)}`)}
                        </Button>
                        {submitError && <div className="text-xs text-red-600 mt-2">{submitError}</div>}
                      </CardFooter>
                    </Card>
                  </div>
                </aside>
 
            </section>
 
        </main>
    );
}