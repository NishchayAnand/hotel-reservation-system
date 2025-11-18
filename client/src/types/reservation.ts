import { ReservationItem } from "./reservationItem"

export type Reservation = {
    id: number;
    hotelId: number;
    checkInDate: string;
    checkOutDate: string;
    amount: number;
    currency: string;
    holdId: number;
    expiresAt: string;
    status: string;
    reservedItems: ReservationItem[];
    createdAt: string;
    updatedAt: string;
}