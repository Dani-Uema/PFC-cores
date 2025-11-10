ALTER TABLE composition
ADD COLUMN id_color UUID;

ALTER TABLE composition
ADD CONSTRAINT fk_composition_color
FOREIGN KEY (id_color) REFERENCES colors(id);