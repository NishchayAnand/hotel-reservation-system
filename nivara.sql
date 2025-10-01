/*
CREATE TABLE locations (
    id      bigserial PRIMARY KEY,
    city    text NOT NULL,
    state   text NOT NULL,
    country text NOT NULL
);


INSERT INTO locations (city, state, country) 
VALUES
    ('Jaipur', 'Rajasthan', 'India'),
    ('Mumbai', 'Maharashtra', 'India'),
    ('Goa', 'Goa', 'India');

SELECT * FROM locations;
*/

/*
CREATE TABLE hotels (
    id              bigserial PRIMARY KEY,
    name            text NOT NULL,
    location_id     bigint REFERENCES locations(id) ON DELETE SET NULL,
    address         text,
    description     text,
    thumbnail_url   text,
    rating          numeric(2,1) CHECK (rating >= 0 AND rating <=5)
);


INSERT INTO public.hotels (name, location_id, address, description, thumbnail_url, rating)
VALUES
  -- Jaipur (id = 1)
  ('Rajasthan Heritage Hotel', 1, 'Bani Park, Jaipur', 'Heritage-style hotel near city palace', NULL, 4.4),
  ('Amber View Suites', 1, 'Near Amber Fort, Jaipur', 'Comfortable suites with fort views', NULL, 4.2),

  -- Mumbai (id = 2)
  ('Marine Drive Residency', 2, 'Marine Drive, Mumbai', 'Sea-facing rooms close to the promenade', NULL, 4.3),
  ('Gateway Business Hotel', 2, 'Fort, Mumbai', 'Business hotel in the financial district', NULL, 4.1),

  -- Goa (id = 3)
  ('Goa Beach Resort', 3, 'Baga Beach, Goa', 'Beachfront resort with pool and spa', NULL, 4.6),
  ('Palolem Retreat', 3, 'Palolem, South Goa', 'Quiet retreat near Palolem beach', NULL, 4.2);

TRUNCATE TABLE hotels RESTART IDENTITY;

SELECT * FROM hotels;
*/

/*
CREATE TABLE amenities (
    id              bigserial PRIMARY KEY,
    name            text NOT NULL UNIQUE,
    description     text
);

DROP TABLE amenities;


INSERT INTO amenities (name, description)
VALUES
  ('wifi', 'Wifi'),
  ('pool', 'Outdoor Swimming Pool'),
  ('parking', 'On-site Parking'),
  ('breakfast', 'Complimentary Breakfast'),
  ('air_conditioning', 'Air Conditioning'),
  ('spa', 'Spa & Wellness Center'),
  ('gym', 'Gym & Fitness Center'),
  ('pet_friendly', 'Pets Allowed'),
  ('airport_shuttle', 'Airport Shuttle'),
  ('room_service', '24/7 Room Service');

SELECT * FROM amenities;

CREATE TABLE hotel_amenities (
    hotel_id    bigint NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    amenity_id  bigint NOT NULL REFERENCES amenities(id) ON DELETE CASCADE,
    PRIMARY KEY (hotel_id, amenity_id)
);

INSERT INTO public.hotel_amenities (hotel_id, amenity_id) VALUES
  -- Jaipur
  (1, 1), -- Rajasthan Heritage Hotel: wifi
  (1, 4), -- breakfast
  (1, 5), -- air_conditioning

  (2, 1), -- Amber View Suites: wifi
  (2, 5), -- air_conditioning
  (2, 10), -- room_service

  -- Mumbai
  (3, 1), -- Marine Drive Residency: wifi
  (3, 3), -- parking
  (3, 5), -- air_conditioning
  (3, 10), -- room_service

  (4, 1), -- Gateway Business Hotel: wifi
  (4, 4), -- breakfast
  (4, 9), -- airport_shuttle
  (4, 10), -- room_service

  -- Goa
  (5, 1), -- Goa Beach Resort: wifi
  (5, 2), -- pool
  (5,3),  -- parking
  (5,6),  -- spa
  (5,10), -- room_service

  (6, 1), -- Palolem Retreat: wifi
  (6, 3), -- parking
  (6, 8), -- pet_friendly
  (6, 4)  -- breakfast

*/