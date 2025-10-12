
export interface Amenity {
  id: string;
  name: string;
  description: string;
}

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
  avgRatePerNight: string
};