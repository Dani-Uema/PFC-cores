CREATE TABLE composition_pigment_proportions (
    composition_id UUID NOT NULL,
    pigment_id UUID NOT NULL,
    proportion DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_composition FOREIGN KEY (composition_id) REFERENCES composition(id) ON DELETE CASCADE,
    CONSTRAINT fk_pigment FOREIGN KEY (pigment_id) REFERENCES pigments(id) ON DELETE CASCADE,

    CONSTRAINT pk_composition_pigment_proportions PRIMARY KEY (composition_id, pigment_id)
);
