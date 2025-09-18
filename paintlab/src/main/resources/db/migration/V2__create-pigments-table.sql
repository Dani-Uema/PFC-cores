CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE pigments(
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    hexCode CHAR(7)
)