package com.example.paintlab.dto.color;
import com.example.paintlab.domain.color.Color;
import com.example.paintlab.dto.PigmentDTO;
import lombok.Data;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ColorDTO {
    private UUID id;
    private String name;
    private String brand;
    private String colorCode;
    private String hexCode;
    private List<PigmentDTO> pigments;

    public ColorDTO(Color color) {
        this.id = color.getId();
        this.name = color.getName();
        this.brand = color.getBrand();
        this.colorCode = color.getColorCode();
        this.hexCode = color.getHexCode();

        this.pigments = color.getCompositions().stream()
                .filter(composition -> composition.getPigmentProportions() != null)
                .flatMap(composition -> composition.getPigmentProportions().entrySet().stream())
                .map(entry -> {
                    PigmentDTO dto = new PigmentDTO();
                    dto.setId(entry.getKey().getId());
                    dto.setName(entry.getKey().getName());
                    dto.setProportion(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
