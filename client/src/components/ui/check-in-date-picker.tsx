"use client"

import * as React from "react"
import { ChevronDownIcon } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { Calendar } from "@/components/ui/calendar"
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover"

export function CheckInDatePicker() {
  const [date, setDate] = React.useState<Date | undefined>(undefined)

  return (
    <div className="flex flex-col gap-2">
        <Label htmlFor="checkindate">Check-in Date</Label>
        <Popover>
            <PopoverTrigger asChild>
                <Button variant="outline" id="checkindate" className="w-48 justify-between font-normal">
                    {date ? date.toLocaleDateString() : "Select date"}
                    <ChevronDownIcon />
                </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto overflow-hidden p-0" align="start">
                <Calendar
                    mode="single"
                    selected={date}
                    captionLayout="dropdown"
                    onSelect={setDate}
                />
            </PopoverContent>
      </Popover>
    </div>
  )
}
