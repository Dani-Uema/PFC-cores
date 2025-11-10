CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE ai_analysis (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    hex_code VARCHAR(7) NOT NULL,
    pigments_data JSONB NOT NULL,
    analysis_date TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ai_analysis_user_id ON ai_analysis(user_id);