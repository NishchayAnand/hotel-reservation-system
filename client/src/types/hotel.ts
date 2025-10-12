
export interface Amenity {
  id: string;
  name: string;
  description: string;
}

export interface Hotel {
  id: string;
  name: string;
  locationId: string;
  address: string;
  description: string;
  thumbnailUrl: string;
  rating: number;
  amenities: Amenity[];
  nights: number
  avgRatePerNight: number
};