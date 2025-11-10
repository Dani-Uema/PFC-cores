package com.example.paintlab.dto;


import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class CompositionDTO {
    private Double percentage;
    private List<PigmentDTO> pigments;
    private Map<UUID, Double> pigmentProportions;
}

