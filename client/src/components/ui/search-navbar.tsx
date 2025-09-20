"use client"

import { useSearchParams } from "next/navigation";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { SearchHotelFormNavbar } from "./navbar-search-hotel-form";

export function SearchNavbar() {
    const searchParams = useSearchParams();
    
      const destination = searchParams.get("destination");
      const checkInDate = searchParams.get("checkInDate");
      const checkOutDate = searchParams.get("checkOutDate");

    return (
        <nav className="fixed left-0 top-0 w-full z-100 h-[4rem] lg:bg-white flex items-center justify-between px-8 border-b">

            {/* Logo */}
            <Link href="/" className="text-lg font-bold">NIVARA</Link>
            
            <SearchHotelFormNavbar
                destination={destination ?? ""}
                checkInDate={checkInDate ? new Date(checkInDate) : undefined}
                checkOutDate={checkOutDate ? new Date(checkOutDate) : undefined}
             />

            {/* Sign In Button */}
            <Button size="sm" asChild>
                <Link href="/signin"> Sign In</Link>
            </Button>
            
        </nav>
    );
}