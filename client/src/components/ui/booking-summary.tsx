"use client"

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
} from "@/components/ui/card";

export default function BookingSummary() {

    return (
        <Card className="w-full shadow-none">
            <CardContent>
                <div className="flex flex-col p-4 bg-gray-50 rounded-lg">
                    <h3 className="text-md font-semibold mb-5">Booking Summary</h3>
                    <div id="pricing-breakdown" className="text-sm">
                        <div className="flex justify-between">
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
            </CardContent>
            <CardFooter>
                <Button type="submit" className="w-full">Book Now</Button>
            </CardFooter>
        </Card>
    );
}