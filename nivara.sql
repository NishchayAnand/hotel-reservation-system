
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

ALTER TABLE hotels RENAME COLUMN description TO short_description;
ALTER TABLE hotels ADD COLUMN long_description TEXT;

UPDATE hotels
SET long_description = CASE id
  WHEN 1 THEN 'A heritage-style property near the City Palace offering traditional Rajasthani decor, spacious suites, an on-site restaurant serving local cuisine, guided cultural tours, and modern amenities for a comfortable stay.'
  WHEN 2 THEN 'Comfortable, well-appointed suites close to Amber Fort with panoramic views, complimentary breakfast, high-speed Wi‑Fi, and concierge services tailored for sightseeing and family stays.'
  WHEN 3 THEN 'Sea-facing rooms along Marine Drive with modern furnishings, easy access to the promenade, business-friendly amenities, in-house dining, and quick transport links to the city center.'
  WHEN 4 THEN 'Business-focused hotel in the financial district offering meeting rooms, express check-in/out, reliable Wi‑Fi, airport transfer options, and compact executive suites.'
  WHEN 5 THEN 'Beachfront resort at Baga with private beach access, outdoor pool, spa services, beach activities, family-friendly facilities, and dining options featuring regional seafood.'
  WHEN 6 THEN 'Quiet seaside retreat near Palolem beach with cottage-style accommodation, relaxed atmosphere, local excursions, pet-friendly options, and personalized hospitality.'
  ELSE long_description
END;

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

CREATE TYPE room_bed_type AS ENUM ('SINGLE', 'DOUBLE');

CREATE TABLE room_types (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL REFERENCES hotels(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    bed_type room_bed_type,
    bed_count INTEGER
);

INSERT INTO room_types (hotel_id, name, description, bed_type, bed_count) VALUES
  (1, 'Standard Room',    'Comfortable room with essential amenities', 'SINGLE', 1),
  (1, 'Deluxe Room',      'Larger room with better view and amenities', 'DOUBLE', 2),
  (1, 'Heritage Suite',   'Spacious suite with heritage decor',       'DOUBLE', 2),

  (2, 'Standard Room',    'Comfortable standard accommodation',       'SINGLE', 1),
  (2, 'Executive Suite',  'Suite suited for business travelers',      'DOUBLE', 1),
  (2, 'Family Room',      'Larger room for families',                 'DOUBLE', 3),

  (3, 'Sea View Room',    'Room with sea-facing view',                'SINGLE', 1),
  (3, 'Standard Room',    'Comfortable standard accommodation',       'SINGLE', 1),
  (3, 'Business Suite',   'Workspace-friendly suite',                 'DOUBLE', 1),

  (4, 'Business Room',    'Room optimized for business stays',        'SINGLE', 1),
  (4, 'Executive Suite',  'Executive-level suite with work area',     'DOUBLE', 1),
  (4, 'Meeting Suite',    'Suite with space for small meetings',      'DOUBLE', 2),

  (5, 'Beachfront Room',  'Room directly facing the beach',           'SINGLE', 1),
  (5, 'Deluxe Sea View',  'Deluxe room with sea view',                'DOUBLE', 2),
  (5, 'Villa Suite',      'Private villa-style suite',                'DOUBLE', 3),

  (6, 'Cottage',          'Cozy cottage near the beach',              'SINGLE', 1),
  (6, 'Family Cottage',   'Spacious cottage for families',            'DOUBLE', 3),
  (6, 'Beach Hut',        'Rustic hut-style accommodation near sand','SINGLE', 1);

INSERT INTO room_amenities (room_type_id, amenity_id) VALUES
  -- hotel 1 (room_type_id 1..3)
  (1, 1), (1, 5), (1, 4),
  (2, 1), (2, 5), (2,10),
  (3, 1), (3, 5), (3, 6),

  -- hotel 2 (room_type_id 4..6)
  (4, 1), (4, 5), (4, 4),
  (5, 1), (5,10), (5, 9),
  (6, 1), (6, 3), (6, 4),

  -- hotel 3 (room_type_id 7..9)
  (7, 1), (7, 2), (7, 5),
  (8, 1), (8, 5),
  (9, 1), (9,10), (9, 9),

  -- hotel 4 (room_type_id 10..12)
  (10, 1), (10, 5),
  (11, 1), (11,10),
  (12, 1), (12,10),

  -- hotel 5 (room_type_id 13..15)
  (13, 1), (13, 2), (13, 6),
  (14, 1), (14, 2), (14,10),
  (15, 1), (15, 2), (15, 6), (15, 8),

  -- hotel 6 (room_type_id 16..18)
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

ALTER TABLE room_type_inventory
ADD CONSTRAINT uq_room_type_inventory_unique
UNIQUE (hotel_id, room_type_id, reservation_date);

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


WITH combos AS (
  SELECT DISTINCT hotel_id, room_type_id
  FROM public.room_type_inventory
),
dates AS (
  SELECT generate_series(
           current_date,
           (current_date + INTERVAL '2 months') - INTERVAL '1 day',
           INTERVAL '1 day'
         )::date AS reservation_date
),
to_insert AS (
  SELECT
    c.hotel_id,
    c.room_type_id,
    d.reservation_date,
    COALESCE(latest.total_count, 0) AS total_count
  FROM combos c
  CROSS JOIN dates d
  LEFT JOIN LATERAL (
    SELECT total_count
    FROM public.room_type_inventory i
    WHERE i.hotel_id = c.hotel_id
      AND i.room_type_id = c.room_type_id
    ORDER BY i.reservation_date DESC
    LIMIT 1
  ) latest ON true
)
INSERT INTO public.room_type_inventory
  (hotel_id, room_type_id, reservation_date, total_count, reserved_count)
SELECT hotel_id, room_type_id, reservation_date, total_count, 0
FROM to_insert;

CREATE TYPE hold_status AS ENUM ('HELD', 'CONFIRMED', 'RELEASED');

CREATE TABLE HOLDS (
    id  BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT UNIQUE NOT NULL,
    hotel_id    BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'HELD',
    expires_at  TIMESTAMPTZ NOT NULL
);

CREATE TABLE hold_items (
    id              BIGSERIAL PRIMARY KEY,
    hold_id         BIGINT NOT NULL REFERENCES holds(id) ON DELETE CASCADE,
    room_type_id    BIGINT  NOT NULL,
    quantity        INTEGER NOT NULL CHECK (quantity > 0)
);

*/

/* PRICING DB */

/*

CREATE TABLE room_type_rate (
    id bigserial PRIMARY KEY,
    hotel_id bigint NOT NULL,
    room_type_id bigint NOT NULL,
    reservation_date date NOT NULL,
    rate numeric(10,2) NOT NULL
);

ALTER TABLE room_type_rate
ADD CONSTRAINT room_type_rate_unique
UNIQUE (hotel_id, room_type_id, reservation_date);

INSERT INTO room_type_rate (hotel_id, room_type_id, reservation_date, rate)
VALUES
    (1, 1, DATE '2025-10-07', 4500),
    (1,	1, DATE	'2025-10-08', 4500),
    (1,	1, DATE	'2025-10-09', 5000),
    (1,	2, DATE	'2025-10-07', 6000),
    (1,	2, DATE	'2025-10-08', 6000),
    (1,	2, DATE	'2025-10-09', 6000),
    (1,	3, DATE	'2025-10-07', 8000),
    (2,	4, DATE	'2025-10-07', 3000),
    (2,	4, DATE	'2025-10-08', 3500),
    (2,	4, DATE	'2025-10-09', 2000),
    (2,	5, DATE	'2025-10-07', 3300),
    (2,	5, DATE	'2025-10-08', 3300),
    (2,	5, DATE	'2025-10-09', 3300),
    (2,	6, DATE	'2025-10-07', 5000),
    (3,	7, DATE	'2025-10-07', 5500),
    (3,	7, DATE	'2025-10-08', 5500),
    (3,	7, DATE	'2025-10-09', 8000),
    (3,	8, DATE	'2025-10-07', 6000),
    (3,	8, DATE	'2025-10-08', 6000),
    (3,	8, DATE	'2025-10-09', 6000),
    (3,	9, DATE	'2025-10-07', 2000),
    (4,	10, DATE '2025-10-07', 10000), 
    (4,	11, DATE '2025-10-07', 12000), 
    (4,	12, DATE '2025-10-07', 14000),
    (5,	13, DATE '2025-10-07', 3500),
    (5,	14, DATE '2025-10-07', 4000),
    (5,	15,	DATE '2025-10-07', 5500),
    (6,	16, DATE '2025-10-07', 3500),
    (6,	17, DATE '2025-10-07', 5000),
    (6,	18, DATE '2025-10-07', 7800);

WITH combos AS (
    SELECT DISTINCT hotel_id, room_type_id
    FROM room_type_rate
),
dates AS (
    SELECT generate_series(
        CURRENT_DATE,
        (CURRENT_DATE + INTERVAL '2 months') - INTERVAL '1 day',
        INTERVAL '1 day'
    )::date AS reservation_date
),
to_insert AS (
    SELECT
        c.hotel_id,
        c.room_type_id,
        d.reservation_date,
        COALESCE(latest.rate, 0) AS rate 
    FROM 
        combos c 
    CROSS JOIN 
        dates d
    LEFT JOIN LATERAL (
        SELECT rate 
        FROM room_type_rate r
        WHERE r.hotel_id = c.hotel_id
            AND r.room_type_id = c.room_type_id
        ORDER BY r.reservation_date DESC
        LIMIT 1
    ) latest ON TRUE
)
INSERT INTO room_type_rate (hotel_id, room_type_id, reservation_date, rate)
SELECT hotel_id, room_type_id, reservation_date, rate
FROM to_insert;

*/

/*

RESERVATION DB

CREATE TABLE reservations (
    id              BIGSERIAL PRIMARY KEY,
    request_id      VARCHAR(255)        NOT NULL UNIQUE,
    hotel_id        BIGINT              NOT NULL,
    check_in_date   DATE                NOT NULL,
    check_out_date  DATE                NOT NULL,
    amount          BIGINT,
    currency        VARCHAR(10),
    payment_id      BIGINT
    hold_id         BIGINT,
    expires_at      TIMESTAMPTZ,
    status          VARCHAR(32)         NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMPTZ         NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ         NOT NULL DEFAULT now()
);

CREATE TABLE reservation_items (
    id              BIGSERIAL PRIMARY KEY,
    reservation_id  BIGINT NOT NULL REFERENCES reservations(id) ON DELETE CASCADE,
    room_type_id    BIGINT NOT NULL,
    name            VARCHAR(255) NOT NULL,
    quantity        INTEGER NOT NULL CHECK (quantity > 0),
    rate            BIGINT NOT NULL CHECK (rate >= 0)
);

*/

/*

PAYMENT SERVICE

CREATE TABLE payments (
    id                  BIGSERIAL PRIMARY KEY,
    reservation_id      BIGINT          NOT NULL UNIQUE,
    hold_id             BIGINT          NOT NULL UNIQUE,
    amount              BIGINT          NOT NULL CHECK (amount >= 0),
    currency            VARCHAR(3)      NOT NULL DEFAULT 'INR',
    status              VARCHAR(32)     NOT NULL DEFAULT 'INITIATED',
    provider_order_id   VARCHAR(255),
    provider_payment_id VARCHAR(255),
    provider_signature  VARCHAR(255),
    guest_name          VARCHAR(255),
    guest_email         VARCHAR(255),
    guest_phone         VARCHAR(255)
);

*/