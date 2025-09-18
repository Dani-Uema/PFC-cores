CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE composition (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    percentage NUMERIC(5,2) NOT NULL CHECK (percentage >= 0 AND percentage <= 100),
    color_id UUID NOT NULL,
    CONSTRAINT fk_color FOREIGN KEY(color_id) REFERENCES colors(id)
);

CREATE TABLE composition_pigments (
    composition_id UUID NOT NULL,
    pigment_id UUID NOT NULL,
    CONSTRAINT pk_composition_pigments PRIMARY KEY (composition_id, pigment_id),
    CONSTRAINT fk_composition FOREIGN KEY (composition_id) REFERENCES composition(id) ON DELETE CASCADE,
    CONSTRAINT fk_pigment FOREIGN KEY (pigment_id) REFERENCES pigments(id) ON DELETE CASCADE
);