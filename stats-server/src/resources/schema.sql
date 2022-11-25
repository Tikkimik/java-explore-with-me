CREATE TABLE IF NOT EXISTS users (
                                     user_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     user_name  VARCHAR(50) NOT NULL,
                                     user_email VARCHAR(50) NOT NULL,
                                     CONSTRAINT PK_USER PRIMARY KEY (user_id),
                                     CONSTRAINT UQ_USER_EMAIL UNIQUE (user_email)
);

