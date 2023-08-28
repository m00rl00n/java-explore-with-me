DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits
(
    id       SERIAL PRIMARY KEY,
    app      VARCHAR(50) NOT NULL,
    uri      VARCHAR(200) NOT NULL,
    ip       VARCHAR(15) NOT NULL,
    datetime TIMESTAMP WITH TIME ZONE,
);

CREATE INDEX idx_app ON hits (app);
CREATE INDEX idx_uri ON hits (uri);