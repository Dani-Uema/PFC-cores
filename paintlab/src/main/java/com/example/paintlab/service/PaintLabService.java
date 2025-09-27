package com.example.paintlab.service;


import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.composition.Composition;
import com.example.paintlab.dto.PigmentCompositionDTO;
import com.example.paintlab.repositories.ColorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaintLabService {
    private final ColorRepository colorRepository;

    public PaintLabService(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    /**
     * Retorna todos os pigmentos de uma cor pelo nome.
     */
    public List<PigmentCompositionDTO> getPigmentsByColorName(String colorName) {
        Optional<Color> optionalColor = colorRepository.findByNameIgnoreCase(colorName);
        if (optionalColor.isEmpty()) return List.of();

        Color color = optionalColor.get();
        List<PigmentCompositionDTO> result = new ArrayList<>();

        for (Composition composition : color.getCompositions()) {
            composition.getPigmentProportions().forEach((pigment, proportion) -> {
                result.add(new PigmentCompositionDTO(
                        pigment.getName(),
                        pigment.getHexCode(),
                        proportion
                ));
            });
        }
        System.out.println("Pigmentoss encontrados: " + result);
        return result;
    }

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }
}
