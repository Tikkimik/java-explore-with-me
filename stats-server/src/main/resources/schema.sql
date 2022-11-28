DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits (
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app       varchar(255),
    uri       VARCHAR(255),
    ip        VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE
);

