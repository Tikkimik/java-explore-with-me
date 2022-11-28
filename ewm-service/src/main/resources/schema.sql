DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS participation_requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events_compilation CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS comments CASCADE;

CREATE TABLE IF NOT EXISTS locations (
    location_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat float,
    lon float
);

CREATE TABLE IF NOT EXISTS users (
    user_id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email              VARCHAR(100) NOT NULL,
    user_name          VARCHAR(50),
    UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name      VARCHAR NOT NULL,
    UNIQUE (category_name)
);

CREATE TABLE IF NOT EXISTS events (
    event_id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000) NOT NULL,
    category           BIGINT,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    initiator          BIGINT,
    location           BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR,
    title              VARCHAR(120),
    views              BIGINT,
    CONSTRAINT fk_location FOREIGN KEY (location) REFERENCES locations (location_id),
    CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories (category_id),
    CONSTRAINT fk_users FOREIGN KEY (initiator) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS requests (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created            TIMESTAMP WITH TIME ZONE NOT NULL,
    event_id           BIGINT NOT NULL,
    requester_id       BIGINT NOT NULL,
    status             VARCHAR NOT NULL,
    CONSTRAINT fk_request_for_event FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT fk_request_for_user FOREIGN KEY (requester_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS events_compilation (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    events_compilation_id   BIGINT NOT NULL,
    compilation_id          BIGINT NOT NULL
);

create table if not exists compilations (
    compilation_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned             BOOLEAN,
    title              VARCHAR
);