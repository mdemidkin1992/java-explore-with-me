DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL
);

CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    rad DOUBLE PRECISION,
    name VARCHAR(100),
    status VARCHAR(100)
);

CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    created_on TIMESTAMP WITH TIME ZONE NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITH TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    participant_limit BIGINT,
    location_id BIGINT NOT NULL,
    published_on TIMESTAMP WITH TIME ZONE,
    state VARCHAR(100),
    title VARCHAR(120) NOT NULL,
    request_moderation BOOLEAN,
    paid BOOLEAN,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(100) NOT NULL,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(225) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE events_compilations (
    event_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_events_compilations_to_events FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_events_compilations_to_compilations FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float
AS
'
declare
    dist float = 0;
    rad_lat1 float;
    rad_lat2 float;
    theta float;
    rad_theta float;
BEGIN
    IF lat1 = lat2 AND lon1 = lon2
    THEN
        RETURN dist;
    ELSE
        -- переводим градусы широты в радианы
        rad_lat1 = pi() * lat1 / 180;
        -- переводим градусы долготы в радианы
        rad_lat2 = pi() * lat2 / 180;
        -- находим разность долгот
        theta = lon1 - lon2;
        -- переводим градусы в радианы
        rad_theta = pi() * theta / 180;
        -- находим длину ортодромии
        dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

        IF dist > 1
            THEN dist = 1;
        END IF;

        dist = acos(dist);
        -- переводим радианы в градусы
        dist = dist * 180 / pi();
        -- переводим градусы в километры
        dist = dist * 60 * 1.8524;

        RETURN dist;
    END IF;
END;
'
LANGUAGE PLPGSQL;