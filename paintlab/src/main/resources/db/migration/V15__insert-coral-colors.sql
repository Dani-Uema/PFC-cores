INSERT INTO colors (id, name, brand, color_code, hex_code) VALUES
('c3d4e5f6-f7f8-9012-cdef-123456789011', 'Branco Neve Coral', 'Coral', 'CL 1001', '#FFFFFF'),
('c3d4e5f6-f7f8-9012-cdef-123456789012', 'Cinza Pérola Coral', 'Coral', 'CL 2015', '#C4C4C4'),
('c3d4e5f6-f7f8-9012-cdef-123456789013', 'Cinza Chumbo Coral', 'Coral', 'CL 2020', '#666666'),
('c3d4e5f6-f7f8-9012-cdef-123456789014', 'Cinza Nuvem Coral', 'Coral', 'CL 2025', '#D3D3D3'),
('c3d4e5f6-f7f8-9012-cdef-123456789015', 'Bege Clássico Coral', 'Coral', 'CL 2008', '#F5F5DC'),
('c3d4e5f6-f7f8-9012-cdef-123456789016', 'Bege Areia Coral', 'Coral', 'CL 2012', '#E6D2B5'),
('c3d4e5f6-f7f8-9012-cdef-123456789017', 'Bege Natural Coral', 'Coral', 'CL 2018', '#E6D7B8'),
('c3d4e5f6-f7f8-9012-cdef-123456789018', 'Marrom Chocolate Coral', 'Coral', 'CL 9010', '#7B3F00'),
('c3d4e5f6-f7f8-9012-cdef-123456789019', 'Marrom Café Coral', 'Coral', 'CL 9005', '#8B4513'),
('c3d4e5f6-f7f8-9012-cdef-123456789020', 'Terra Batida Coral', 'Coral', 'CL 9105', '#8B7355'),
('c3d4e5f6-f7f8-9012-cdef-123456789021', 'Azul Marinho Coral', 'Coral', 'CL 5008', '#003366'),
('c3d4e5f6-f7f8-9012-cdef-123456789022', 'Azul Celeste Coral', 'Coral', 'CL 5012', '#87CEEB'),
('c3d4e5f6-f7f8-9012-cdef-123456789023', 'Azul Turquesa Coral', 'Coral', 'CL 5018', '#40E0D0'),
('c3d4e5f6-f7f8-9012-cdef-123456789024', 'Azul Royal Coral', 'Coral', 'CL 5022', '#4169E1'),
('c3d4e5f6-f7f8-9012-cdef-123456789025', 'Verde Floresta Coral', 'Coral', 'CL 6012', '#228B22'),
('c3d4e5f6-f7f8-9012-cdef-123456789026', 'Verde Limão Coral', 'Coral', 'CL 6018', '#32CD32'),
('c3d4e5f6-f7f8-9012-cdef-123456789027', 'Verde Água Coral', 'Coral', 'CL 6022', '#7FFFD4'),
('c3d4e5f6-f7f8-9012-cdef-123456789028', 'Verde Musgo Coral', 'Coral', 'CL 6028', '#8A9A5B'),
('c3d4e5f6-f7f8-9012-cdef-123456789029', 'Amarelo Canário Coral', 'Coral', 'CL 4002', '#FFFF99'),
('c3d4e5f6-f7f8-9012-cdef-123456789030', 'Amarelo Ouro Coral', 'Coral', 'CL 4010', '#FFD700'),
('c3d4e5f6-f7f8-9012-cdef-123456789031', 'Amarelo Mostarda Coral', 'Coral', 'CL 4015', '#FFDB58'),
('c3d4e5f6-f7f8-9012-cdef-123456789032', 'Laranja Sunset Coral', 'Coral', 'CL 3008', '#FF7F50'),
('c3d4e5f6-f7f8-9012-cdef-123456789033', 'Laranja Coral Coral', 'Coral', 'CL 3020', '#FF7F50'),
('c3d4e5f6-f7f8-9012-cdef-123456789034', 'Laranja Queimado Coral', 'Coral', 'CL 3015', '#CC5500'),
('c3d4e5f6-f7f8-9012-cdef-123456789035', 'Vermelho Passion Coral', 'Coral', 'CL 3004', '#FF6B6B'),
('c3d4e5f6-f7f8-9012-cdef-123456789036', 'Roxo Imperial Coral', 'Coral', 'CL 7005', '#6A0DAD'),
('c3d4e5f6-f7f8-9012-cdef-123456789037', 'Roxo Suave Coral', 'Coral', 'CL 7010', '#D8BFD8'),
('c3d4e5f6-f7f8-9012-cdef-123456789038', 'Roxo Profundo Coral', 'Coral', 'CL 7015', '#4B0082'),
('c3d4e5f6-f7f8-9012-cdef-123456789039', 'Rosa Bebê Coral', 'Coral', 'CL 8003', '#F4C2C2'),
('c3d4e5f6-f7f8-9012-cdef-123456789040', 'Rosa Choque Coral', 'Coral', 'CL 8008', '#FF007F')
ON CONFLICT (id) DO NOTHING;

-- Insert compositions
INSERT INTO composition (id, percentage, id_color) VALUES
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789011'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789012'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789013'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789014'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789015'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789016'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789017'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789018'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789019'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789020'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789021'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789022'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789023'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789024'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789025'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789026'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789027'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789028'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789029'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789030'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789031'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789032'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789033'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789034'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789035'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789036'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789037'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789038'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789039'),
(gen_random_uuid(), 100.0, 'c3d4e5f6-f7f8-9012-cdef-123456789040');

-- Insert composition_pigments e composition_pigment_proportions (combinações realistas de pigmentos)
DO $$
DECLARE
    comp_id UUID;
BEGIN
    -- Branco Neve Coral (sem pigmentos - cor base)

    -- Cinza Pérola Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789012';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 8.0);

    -- Cinza Chumbo Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789013';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 25.0);

    -- Cinza Nuvem Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789014';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 12.0);

    -- Bege Clássico Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789015';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 6.0);

    -- Bege Areia Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789016';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836'),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 9.0),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 1.5);

    -- Bege Natural Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789017';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 7.0);

    -- Marrom Chocolate Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789018';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 35.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 5.0);

    -- Marrom Café Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789019';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505'),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 28.0),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 4.0);

    -- Terra Batida Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789020';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836'),
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 20.0),
    (comp_id, '4a64da73-0c8e-42b1-9658-b1e86288b505', 8.0);

    -- Azul Marinho Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789021';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339'),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 40.0),
    (comp_id, '532d5ce5-2323-4d3b-b30f-0328b2b9ba89', 8.0);

    -- Azul Celeste Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789022';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 10.0);

    -- Azul Turquesa Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789023';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339'),
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 15.0),
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 5.0);

    -- Azul Royal Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789024';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 25.0);

    -- Verde Floresta Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789025';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 30.0);

    -- Verde Limão Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789026';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 18.0);

    -- Verde Água Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789027';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552'),
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 12.0),
    (comp_id, 'dd1da550-a0b1-4bfa-aad1-154779019339', 4.0);

    -- Verde Musgo Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789028';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552'),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '1180d92b-303a-4fe3-9fa7-38baa09c5552', 22.0),
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 4.0);

    -- Amarelo Canário Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789029';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 15.0);

    -- Amarelo Ouro Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789030';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae'),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 20.0),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 3.0);

    -- Amarelo Mostarda Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789031';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae'),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '853854b7-fb0d-4d1d-85a5-087326fd47ae', 18.0),
    (comp_id, 'b515b239-9f12-453a-a732-3e7497dc9836', 5.0);

    -- Laranja Sunset Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789032';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0', 20.0);

    -- Laranja Coral Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789033';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0', 22.0);

    -- Laranja Queimado Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789034';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0'),
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, 'ac3f5922-4234-4506-82cf-c3bc42d5a9e0', 25.0),
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 5.0);

    -- Vermelho Passion Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789035';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 28.0);

    -- Roxo Imperial Coral (2 pigmentos)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789036';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES
    (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c'),
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES
    (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c', 35.0),
    (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 8.0);

    -- Roxo Suave Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789037';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c', 12.0);

    -- Roxo Profundo Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789038';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, '3721d209-086e-4500-b8ac-c9545bec6a2c', 38.0);

    -- Rosa Bebê Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789039';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 6.0);

    -- Rosa Choque Coral (1 pigmento)
    SELECT id INTO comp_id FROM composition WHERE id_color = 'c3d4e5f6-f7f8-9012-cdef-123456789040';
    INSERT INTO composition_pigments (composition_id, pigment_id) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc');
    INSERT INTO composition_pigment_proportions (composition_id, pigment_id, proportion) VALUES (comp_id, 'ec5a7f34-0c7a-41b5-b88d-b141ae9e58bc', 24.0);

END $$;