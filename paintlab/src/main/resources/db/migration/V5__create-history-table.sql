CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE history (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id UUID NOT NULL,
    color_id UUID NOT NULL,
    consultation_date TIMESTAMP DEFAULT now(),

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_color FOREIGN KEY (color_id) REFERENCES colors(id)
);
