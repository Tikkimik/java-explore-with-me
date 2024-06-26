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
    user_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email              VARCHAR(100) UNIQUE NOT NULL,
    user_name          VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS categories (
    category_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    category_name      VARCHAR UNIQUE NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (category_id)
);

CREATE TABLE IF NOT EXISTS events (
    event_id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000) NOT NULL,
    category           BIGINT NOT NULL,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    initiator          BIGINT NOT NULL,
    location           BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    confirmed_requests BIGINT,
    state              VARCHAR,
    title              VARCHAR(120),
    views              BIGINT,
    CONSTRAINT pk_events PRIMARY KEY (event_id),
    CONSTRAINT initiator FOREIGN KEY(initiator) REFERENCES users(user_id),
    CONSTRAINT event_category FOREIGN KEY(category) REFERENCES categories(category_id),
    CONSTRAINT event_location FOREIGN KEY(location) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS requests (
    request_id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    initiator          BIGINT NOT NULL,
    created            TIMESTAMP WITH TIME ZONE NOT NULL,
    event              BIGINT NOT NULL,
    requester          BIGINT NOT NULL,
    status             VARCHAR NOT NULL,
    CONSTRAINT pk_request PRIMARY KEY (request_id),
    CONSTRAINT fk_request_for_event FOREIGN KEY (event) REFERENCES events (event_id),
    CONSTRAINT fk_initiator FOREIGN KEY (initiator) REFERENCES users (user_id),
    CONSTRAINT fk_request_for_user FOREIGN KEY (requester) REFERENCES users (user_id)
);

create table if not exists compilations (
    compilation_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned             BOOLEAN,
    title              VARCHAR
);

CREATE TABLE IF NOT EXISTS compilation_events (
     compilation_events_id BIGINT NOT NULL,
     event_id BIGINT NOT NULL,
     CONSTRAINT fk_compilation FOREIGN KEY (compilation_events_id) REFERENCES compilations(compilation_id),
     CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(event_id)
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    message  VARCHAR(280),
    event_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    created  TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_comments_for_user FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_comments_for_event FOREIGN KEY (event_id) REFERENCES events (event_id)
);
