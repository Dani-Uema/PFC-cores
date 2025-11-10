CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE saved_colors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    color_code VARCHAR(100) NOT NULL,
    color_name VARCHAR(200) NOT NULL,
    environment_tag VARCHAR(150) NOT NULL,
    notes TEXT,
    saved_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_saved_colors_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_saved_colors_user_id ON saved_colors(user_id);
CREATE INDEX idx_saved_colors_saved_date ON saved_colors(saved_date DESC);
CREATE INDEX idx_saved_colors_environment_tag ON saved_colors(environment_tag);