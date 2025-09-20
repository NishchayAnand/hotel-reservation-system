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

// Sample destinations
const destinations = [
    { label: "Manali", value: "Manali" },
    { label: "Jaipur", value: "Maipur" },
    { label: "Udaipur", value: "Udaipur" },
    { label: "Mumbai", value: "Mumbai" },
    { label: "Kerala", value: "Kerala" }
];

// Interface for form values
type FormValues = {
    destination?: string;
    checkInDate?: Date | undefined;
    checkOutDate?: Date | undefined;
};

export function SearchHotelFormNavbar(props: FormValues) {
    const [isDestPopOpen, setIsDestPopOpen] = useState(false);
    const [isCheckInPopOpen, setIsCheckInPopOpen] = useState(false);
    const [isCheckOutPopOpen, setIsCheckOutPopOpen] = useState(false);

    const form = useForm<FormValues>({
        defaultValues: {
            destination: props.destination ?? "",
            checkInDate: props.checkInDate ?? undefined,
            checkOutDate: props.checkOutDate ?? undefined
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
            destination: data.destination,
            checkInDate: data.checkInDate.toISOString().split("T")[0],
            checkOutDate: data.checkOutDate.toISOString().split("T")[0],
        }).toString();

        // api call
    }

    return (

    <Form {...form}>           
        <form
            onSubmit={form.handleSubmit(onSubmit)}   
            className="flex flex-col justify-center items-center gap-6 lg:flex-row lg:flex-wrap"
        >                    
            {/* Destination */}
            <FormField
                name="destination"
                control={form.control}
                render={({ field }) => (
                    <FormItem className="flex flex-col gap-2">
                        <FormControl>
                            <Popover open={isDestPopOpen} onOpenChange={setIsDestPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-48 justify-between font-normal">
                                        {field.value 
                                            ? destinations.find(dest => dest.value === field.value)?.label 
                                            : "Select destination"
                                        }
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0" align="start">
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
                    <FormItem className="flex flex-col gap-2">
                        <FormControl>
                            <Popover open={isCheckInPopOpen} onOpenChange={setIsCheckInPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-48 justify-between font-normal">
                                        { field.value ? field.value.toLocaleDateString() : "Select date"}
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0" align="start">
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
                    <FormItem className="flex flex-col gap-2">
                        <FormControl>
                            <Popover open={isCheckOutPopOpen} onOpenChange={setIsCheckOutPopOpen}>
                                <PopoverTrigger asChild>
                                    <Button variant="outline" className="w-48 justify-between font-normal">
                                        { field.value ? field.value.toLocaleDateString() : "Select date"}
                                        <ChevronDownIcon />
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto overflow-hidden p-0" align="start">
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
            <Button type="submit" className="w-48 mt-auto">Search</Button>
            
        </form>
    </Form>
);
}