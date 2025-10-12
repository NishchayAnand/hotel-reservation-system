"use client"

import { useState } from "react";
import { useForm } from "react-hook-form";
import { Check, ChevronDownIcon } from "lucide-react"
import { cn } from "@/lib/utils";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover";
import { Command, CommandList, CommandEmpty, CommandGroup, CommandInput, CommandItem } from "@/components/ui/command";
import { Form, FormControl, FormField, FormItem, FormLabel } from "@/components/ui/form";

// Interface for Destination
interface Destination {
    label: string;
    value: string;
}

// Interface for form values
type FormValues = {
    destination: string;
    checkInDate: Date | undefined;
    checkOutDate: Date | undefined;
};

function pad(n: number) {
    return String(n).padStart(2,"0"); // pads the current string n with '0' until it reaches the specified length (here, 2).
}

function formatDateLocal(d: Date) {
    return `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
}

export function SearchHotelForm( {destinations} : {destinations: Destination[]} ) {
    const [isDestPopOpen, setIsDestPopOpen] = useState(false);
    const [isCheckInPopOpen, setIsCheckInPopOpen] = useState(false);
    const [isCheckOutPopOpen, setIsCheckOutPopOpen] = useState(false);

    const router = useRouter();

    const form = useForm<FormValues>({
        defaultValues: {
            destination: "",
            checkInDate: undefined,
            checkOutDate: undefined
        }
    });

    function onSubmit(data: FormValues) {
        // check if destination is empty
        if(!data.destination) {
            setIsDestPopOpen(true);
            return;
        }

        // check if check-in date is empty
        if(!data.checkInDate) {
            setIsCheckInPopOpen(true);
            return;
        }

        // check if check-out date is empty
        if(!data.checkOutDate) {
            setIsCheckOutPopOpen(true);
            return;
        }
        const query = new URLSearchParams({
            locationId: data.destination,
            checkInDate: formatDateLocal(data.checkInDate),
            checkOutDate: formatDateLocal(data.checkOutDate),
        }).toString();

        router.push(`/hotel-listings?${query}`);
    }

    return (

    <Form {...form}>           
        <form
            onSubmit={form.handleSubmit(onSubmit)}   
            className="flex flex-col gap-6 lg:flex-row lg:items-center"
        >                    
            {/* Destination */}
            <FormField
                name="destination"
                control={form.control}
                render={({ field }) => (
                    <FormItem className="flex-1">
                        <FormLabel>Destination</FormLabel>
                        <FormControl>
                            <Popover open={isDestPopOpen} onOpenChange={setIsDestPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="flex justify-between font-normal cursor-pointer">
                                        {field.value 
                                            ? destinations.find(dest => dest.value === field.value)?.label 
                                            : "Select destination"
                                        }
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0 z-300" align="start">
                                    <Command>
                                        <CommandInput placeholder="Search Destination..." className="h-9" />
                                        <CommandList>
                                            <CommandEmpty>No Destination found.</CommandEmpty>
                                            <CommandGroup>
                                                {destinations.map((dest) => (
                                                    <CommandItem
                                                        key={dest.value}
                                                        value={dest.value}
                                                        onSelect={(currentDestination) => {
                                                            // set the form field value
                                                            field.onChange(currentDestination);
                                                            // close the popover
                                                            setIsDestPopOpen(false);
                                                        }}
                                                    >
                                                        {dest.label}
                                                        <Check
                                                            className={cn(
                                                                "ml-auto",
                                                                field.value === dest.value ? "opacity-100" : "opacity-0"
                                                            )}
                                                        />
                                                    </CommandItem>
                                                ))}
                                            </CommandGroup>
                                        </CommandList>
                                    </Command>
                                </PopoverContent>
                            </Popover>
                        </FormControl>
                    </FormItem>
                )}
            />

            {/* Check-in Date */}
            <FormField
                name="checkInDate"
                control={form.control}
                render={({ field}) => (
                    <FormItem className="flex-1">
                        <FormLabel>Check-In Date</FormLabel>
                        <FormControl>
                            <Popover open={isCheckInPopOpen} onOpenChange={setIsCheckInPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="flex justify-between font-normal cursor-pointer">
                                        { field.value ? field.value.toLocaleDateString() : "Select date"}
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0 z-300" align="start">
                                    <Calendar
                                        mode="single"
                                        selected={field.value} 
                                        captionLayout="dropdown"
                                        onSelect={(date) => {
                                            field.onChange(date);
                                            setIsCheckInPopOpen(false);
                                        }}
                                        disabled={(date) => date.getTime() < new Date().setHours(0,0,0,0)} // disable dates before today
                                    />
                                </PopoverContent>
                            </Popover>
                        </FormControl>
                    </FormItem>
                )}
            />

            {/* Check-out Date */}
            <FormField
                name="checkOutDate"
                control={form.control}
                render={({ field}) => (
                    <FormItem className="flex-1">
                        <FormLabel>Check-Out Date</FormLabel>
                        <FormControl>
                            <Popover open={isCheckOutPopOpen} onOpenChange={setIsCheckOutPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="flex justify-between font-normal cursor-pointer">
                                        { field.value ? field.value.toLocaleDateString() : "Select date"}
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0 z-300" align="start">
                                    <Calendar
                                        mode="single"
                                        selected={field.value}
                                        captionLayout="dropdown"
                                        onSelect={(date) => {
                                            field.onChange(date);
                                            setIsCheckOutPopOpen(false);
                                        }}
                                        disabled={(date) => {
                                            const checkInDate = form.getValues("checkInDate");
                                            if(!checkInDate) {
                                                return true; // Disable all dates if no check-in date is selected
                                            }
                                            return date <= checkInDate; // Disable dates before or on the check-in date
                                        }}
                                    />
                                </PopoverContent>
                            </Popover>
                        </FormControl>
                    </FormItem>
                )}
            />

            {/* Search Button */}
            <Button type="submit" className="flex-1 mt-auto cursor-pointer">Search</Button>
            
        </form>
    </Form>
);
}