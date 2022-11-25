DROP TABLE IF EXISTS  stat CASCADE;

CREATE TABLE IF NOT EXISTS stat (
    stat_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app     VARCHAR(50),
    uri     VARCHAR(200),
    ip      VARCHAR(50),
    TIMESTAMP timestamp with time zone
);