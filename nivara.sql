
/* HOTEL DB */

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

/*

CREATE TABLE room_types (
    id          bigserial PRIMARY KEY,
    hotel_id    bigint NOT NULL REFERENCES hotels(id),
    name        text NOT NULL,
    description text,
    beds        int
);

INSERT INTO room_types (hotel_id, name, description, beds) 
VALUES
  -- Hotel 1
  (1, 'Standard Room',    'Comfortable room with essential amenities',      1),
  (1, 'Deluxe Room',      'Larger room with better view and amenities',     2),
  (1, 'Heritage Suite',   'Spacious suite with heritage decor',             2),

  -- Hotel 2
  (2, 'Standard Room',    'Comfortable standard accommodation',             1),
  (2, 'Executive Suite',  'Suite suited for business travelers',            1),
  (2, 'Family Room',      'Larger room for families',                       3),

  -- Hotel 3
  (3, 'Sea View Room',    'Room with sea-facing view',                      1),
  (3, 'Standard Room',    'Comfortable standard accommodation',             1),
  (3, 'Business Suite',   'Workspace-friendly suite for business guests',   1),

  -- Hotel 4
  (4, 'Business Room',    'Room optimized for business stays',               1),
  (4, 'Executive Suite',  'Executive-level suite with work area',            1),
  (4, 'Meeting Suite',    'Suite with space for small meetings',             2),

  -- Hotel 5
  (5, 'Beachfront Room',  'Room directly facing the beach',                  1),
  (5, 'Deluxe Sea View',  'Deluxe room with sea view',                       2),
  (5, 'Villa Suite',      'Private villa-style suite',                       3),

  -- Hotel 6
  (6, 'Cottage',          'Cozy cottage near the beach',                     1),
  (6, 'Family Cottage',   'Spacious cottage for families',                   3),
  (6, 'Beach Hut',        'Rustic hut-style accommodation near the sand',    1);

select * from room_types;

*/

/*
CREATE TABLE room_amenities (
    room_type_id bigint NOT NULL REFERENCES room_types(id) ON DELETE CASCADE,
    amenity_id   bigint NOT NULL REFERENCES amenities(id) ON DELETE CASCADE
);

INSERT INTO public.room_amenities (room_type_id, amenity_id) 
VALUES
  -- room_types id 1..3 (hotel 1)
  (1, 1), (1, 5), (1, 4),
  (2, 1), (2, 5), (2,10),
  (3, 1), (3, 5), (3, 6),

  -- room_types id 4..6 (hotel 2)
  (4, 1), (4, 5), (4, 4),
  (5, 1), (5,10), (5, 9),
  (6, 1), (6, 3), (6, 4),

  -- room_types id 7..9 (hotel 3)
  (7, 1), (7, 2), (7, 5),
  (8, 1), (8, 5),
  (9, 1), (9,10), (9, 9),

  -- room_types id 10..12 (hotel 4)
  (10, 1), (10, 5),
  (11, 1), (11,10),
  (12, 1), (12,10),

  -- room_types id 13..15 (hotel 5)
  (13, 1), (13, 2), (13, 6),
  (14, 1), (14, 2), (14,10),
  (15, 1), (15, 2), (15, 6), (15, 8),

  -- room_types id 16..18 (hotel 6)
  (16, 1), (16, 8),
  (17, 1), (17, 4), (17, 3),
  (18, 1), (18, 8);

*/

/* INVENTORY DB */

/*

CREATE TABLE room_type_inventory (
    id bigserial PRIMARY KEY,
    hotel_id bigint NOT NULL,
    room_type_id bigint NOT NULL,
    reservation_date date NOT NULL,
    total_count integer NOT NULL DEFAULT 0,
    reserved_count integer NOT NULL DEFAULT 0
);

INSERT INTO room_type_inventory (hotel_id, room_type_id, reservation_date, total_count, reserved_count)
VALUES
  -- Hotel 1
  (1, 1, CURRENT_DATE, 10, 2),
  (1, 2, CURRENT_DATE,  5, 1),
  (1, 3, CURRENT_DATE,  2, 0),

  -- Hotel 2
  (2, 4, CURRENT_DATE,  8, 3),
  (2, 5, CURRENT_DATE,  3, 1),
  (2, 6, CURRENT_DATE,  4, 2),

  -- Hotel 3
  (3, 7, CURRENT_DATE,  6, 2),
  (3, 8, CURRENT_DATE, 10, 4),
  (3, 9, CURRENT_DATE,  2, 0),

  -- Hotel 4
  (4,10, CURRENT_DATE,   5, 1),
  (4,11, CURRENT_DATE,   2, 0),
  (4,12, CURRENT_DATE,   1, 0),

  -- Hotel 5
  (5,13, CURRENT_DATE,  12, 6),
  (5,14, CURRENT_DATE,   6, 2),
  (5,15, CURRENT_DATE,   2, 0),

  -- Hotel 6
  (6,16, CURRENT_DATE,   3, 1),
  (6,17, CURRENT_DATE,   4, 1),
  (6,18, CURRENT_DATE,   2, 0);

*/