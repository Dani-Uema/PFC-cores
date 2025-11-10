package com.example.paintlab.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PigmentCompositionDTO {
    private String name;
    private String hexCode;
    private Double proportion;
}