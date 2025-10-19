import { Amenity } from "./amenity";

export interface Hotel {
  id: string;
  name: string;
  address: string;
  shortDescription: string;
  longDescription: string;
  thumbnailUrl: string;
  rating: string;
  amenities: Amenity[];
  nights: string;
  minAvgRatePerNight: string
};