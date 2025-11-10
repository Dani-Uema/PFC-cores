-- Insert Suvinil colors (UUIDs VÁLIDOS)
INSERT INTO colors (id, name, brand, color_code, hex_code) VALUES
-- Brancos e Neutros
('d4e5f6f7-f8f9-0123-def0-123456789001', 'SV Branco Gelo', 'Suvinil', 'SN 002', '#F0F0F0'),
('d4e5f6f7-f8f9-0123-def0-123456789002', 'SV Branco Neve', 'Suvinil', 'SN 001', '#F5F5F5'),
('d4e5f6f7-f8f9-0123-def0-123456789003', 'SV Marfim', 'Suvinil', 'SN 010', '#F5EBDC'),

-- Cinzas
('d4e5f6f7-f8f9-0123-def0-123456789004', 'SV Cinza Oxford', 'Suvinil', 'SN 301', '#4A4A4A'),
('d4e5f6f7-f8f9-0123-def0-123456789005', 'SV Cinza Chumbo', 'Suvinil', 'CN 015', '#666666'),
('d4e5f6f7-f8f9-0123-def0-123456789006', 'SV Cinza Nuvem', 'Suvinil', 'SN 305', '#BEBEBE'),

-- Terrosos e Bege
('d4e5f6f7-f8f9-0123-def0-123456789007', 'SV Areia Movediça', 'Suvinil', 'SN 412', '#D7C7A6'),
('d4e5f6f7-f8f9-0123-def0-123456789008', 'SV Bege Claro', 'Suvinil', 'SN 408', '#E6D9C4'),
('d4e5f6f7-f8f9-0123-def0-123456789009', 'SV Terra Queimada', 'Suvinil', 'SN 425', '#8B7355'),

-- Amarelos e Laranjas
('d4e5f6f7-f8f9-0123-def0-123456789010', 'SV Amarelo Sol', 'Suvinil', 'AM 004', '#FFDE59'),
('d4e5f6f7-f8f9-0123-def0-123456789011', 'SV Amarelo Ouro', 'Suvinil', 'AM 012', '#E6B200'),
('d4e5f6f7-f8f9-0123-def0-123456789012', 'SV Laranja Energia', 'Suvinil', 'LA 003', '#FF8C00'),
('d4e5f6f7-f8f9-0123-def0-123456789013', 'SV Laranja Suave', 'Suvinil', 'LA 007', '#FFB366'),

-- Vermelhos e Rosas
('d4e5f6f7-f8f9-0123-def0-123456789014', 'SV Vermelho Ferrari', 'Suvinil', 'VM 005', '#CC0000'),
('d4e5f6f7-f8f9-0123-def0-123456789015', 'SV Vermelho Amor', 'Suvinil', 'VM 010', '#B30000'),
('d4e5f6f7-f8f9-0123-def0-123456789016', 'SV Vinho Bordeaux', 'Suvinil', 'VM 025', '#660033'),
('d4e5f6f7-f8f9-0123-def0-123456789017', 'SV Rosa Baby', 'Suvinil', 'RS 002', '#FFB6C1'),
('d4e5f6f7-f8f9-0123-def0-123456789018', 'SV Rosa Choque', 'Suvinil', 'RS 008', '#FF69B4'),

-- Lilás e Violeta
('d4e5f6f7-f8f9-0123-def0-123456789019', 'SV Lilás Serenidade', 'Suvinil', 'LI 004', '#C8A2C8'),
('d4e5f6f7-f8f9-0123-def0-123456789020', 'SV Violeta Intenso', 'Suvinil', 'VI 006', '#8A2BE2'),

-- Azuis
('d4e5f6f7-f8f9-0123-def0-123456789021', 'SV Azul Celeste', 'Suvinil', 'AZ 008', '#87CEEB'),
('d4e5f6f7-f8f9-0123-def0-123456789022', 'SV Azul Royal', 'Suvinil', 'AZ 015', '#4169E1'),
('d4e5f6f7-f8f9-0123-def0-123456789023', 'SV Azul Marinho', 'Suvinil', 'AZ 030', '#003366'),
('d4e5f6f7-f8f9-0123-def0-123456789024', 'SV Azul Petróleo', 'Suvinil', 'AZ 035', '#005B7F'),

-- Verdes
('d4e5f6f7-f8f9-0123-def0-123456789025', 'SV Verde Limão', 'Suvinil', 'VD 005', '#32CD32'),
('d4e5f6f7-f8f9-0123-def0-123456789026', 'SV Verde Floresta', 'Suvinil', 'VD 018', '#228B22'),
('d4e5f6f7-f8f9-0123-def0-123456789027', 'SV Verde Musgo', 'Suvinil', 'VD 025', '#6B8E23'),
('d4e5f6f7-f8f9-0123-def0-123456789028', 'SV Verde Água', 'Suvinil', 'VD 040', '#00CED1'),

-- Marrons
('d4e5f6f7-f8f9-0123-def0-123456789029', 'SV Marrom Chocolate', 'Suvinil', 'MR 010', '#5C3317'),
('d4e5f6f7-f8f9-0123-def0-123456789030', 'SV Marrom Canela', 'Suvinil', 'MR 005', '#D2691E')
ON CONFLICT (id) DO NOTHING;

-- Insert compositions
INSERT INTO composition (id, percentage, id_color) VALUES
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789001'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789002'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789003'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789004'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789005'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789006'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789007'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789008'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789009'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789010'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789011'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789012'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789013'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789014'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789015'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789016'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789017'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789018'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789019'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789020'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789021'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789022'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789023'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789024'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789025'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789026'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789027'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789028'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789029'),
(gen_random_uuid(), 100.0, 'd4e5f6f7-f8f9-0123-def0-123456789030');

-- Insert composition_pigments e composition_pigment_proportions (combinações reais de pigmentos)
DO $$
DECLARE
    comp_id UUID;
BEGIN
    -- SV Branco Gelo (sem pigmentos - cor base)
    -- SV Branco Neve (sem pigmentos - cor base)

    -- SV Marfim (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789003';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 5.0);

    -- SV Cinza Oxford (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789004';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 35.0);

    -- SV Cinza Chumbo (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789005';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 25.0);

    -- SV Cinza Nuvem (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789006';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 15.0);

    -- SV Areia Movediça (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789007';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836'),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 10.0),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 2.0);

    -- SV Bege Claro (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789008';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 8.0);

    -- SV Terra Queimada (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789009';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836'),
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 25.0),
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 5.0);

    -- SV Amarelo Sol (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789010';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 20.0);

    -- SV Amarelo Ouro (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789011';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae'),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 25.0),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 5.0);

    -- SV Laranja Energia (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789012';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0', 25.0);

    -- SV Laranja Suave (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789013';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0', 15.0);

    -- SV Vermelho Ferrari (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789014';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 30.0);

    -- SV Vermelho Amor (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789015';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 35.0);

    -- SV Vinho Bordeaux (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789016';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 45.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 5.0);

    -- SV Rosa Baby (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789017';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 8.0);

    -- SV Rosa Choque (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789018';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 20.0);

    -- SV Lilás Serenidade (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789019';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c', 15.0);

    -- SV Violeta Intenso (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789020';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c', 40.0);

    -- SV Azul Celeste (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789021';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 12.0);

    -- SV Azul Royal (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789022';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 30.0);

    -- SV Azul Marinho (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789023';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 45.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 10.0);

    -- SV Azul Petróleo (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789024';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 35.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 5.0);

    -- SV Verde Limão (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789025';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 20.0);

    -- SV Verde Floresta (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789026';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 35.0);

    -- SV Verde Musgo (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789027';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552'),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 25.0),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 5.0);

    -- SV Verde Água (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789028';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552'),
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 15.0),
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 5.0);

    -- SV Marrom Chocolate (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789029';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 40.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 5.0);

    -- SV Marrom Canela (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'd4e5f6f7-f8f9-0123-def0-123456789030';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505'),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 20.0),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 5.0);

END $$;