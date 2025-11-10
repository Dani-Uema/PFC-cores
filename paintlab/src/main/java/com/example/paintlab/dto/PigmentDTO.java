package com.example.paintlab.dto;

import lombok.Data;

import java.util.UUID;


@Data
public class PigmentDTO {
    private UUID id;
    private Double proportion;
    private String name;
}
