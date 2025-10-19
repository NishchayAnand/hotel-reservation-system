import { Amenity } from "./amenity";

export interface RoomType {
  id: string;
  name: string;
  description: string;
  bedType: string;
  bedCount: string;
  thumbnailUrl: string;
  availableRoomCount: number;
  avgPricePerNight: number;
  amenities: Amenity[];
}