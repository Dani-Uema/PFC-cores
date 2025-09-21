CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE colors(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    brand VARCHAR(100) NOT NULL,
    colorCode VARCHAR(50),
    hexCode CHAR(7)
)