package com.example.paintlab.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PigmentCompositionDTO {
    private String pigmentName;
    private String pigmentHex;
    private Double proportion;
}
