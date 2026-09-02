ALTER TABLE ski_resorts
    ADD COLUMN mid_altitude INTEGER;

UPDATE ski_resorts
SET mid_altitude = ((base_altitude + top_altitude) / 2);

ALTER TABLE ski_resorts
    ALTER COLUMN mid_altitude SET NOT NULL;

ALTER TABLE ski_resorts
    ADD CONSTRAINT ck_ski_resorts_mid_altitude CHECK (mid_altitude > 0),
    ADD CONSTRAINT ck_ski_resorts_mid_altitude_range CHECK (
        mid_altitude >= base_altitude AND mid_altitude <= top_altitude
    );
