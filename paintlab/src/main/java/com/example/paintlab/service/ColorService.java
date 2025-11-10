package com.example.paintlab.service;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.composition.Composition;
import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.*;
import com.example.paintlab.dto.color.ColorCreateDTO;
import com.example.paintlab.dto.color.ColorDTO;
import com.example.paintlab.dto.color.ColorUpdateDTO;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.PigmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ColorService {
    private final ColorRepository colorRepository;
    private final PigmentRepository pigmentRepository;

    public ColorService(ColorRepository colorRepository, PigmentRepository pigmentRepository) {
        this.colorRepository = colorRepository;
        this.pigmentRepository = pigmentRepository;
    }

    public List<Color> searchColors(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return colorRepository.findAll();
        }
        return colorRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCaseOrColorCodeContainingIgnoreCase(
                searchTerm.trim(), searchTerm.trim(), searchTerm.trim()
        );
    }

    public Color getColorById(UUID id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found with id: " + id));
    }

    public List<PigmentCompositionDTO> getPigmentsByColorId(UUID colorId) {
        Color color = getColorById(colorId);
        List<PigmentCompositionDTO> result = new ArrayList<>();

        for (Composition composition : color.getCompositions()) {
            if (composition.getPigmentProportions() != null) {
                composition.getPigmentProportions().forEach((pigment, proportion) -> {
                    PigmentCompositionDTO dto = new PigmentCompositionDTO();
                    dto.setName(pigment.getName());
                    dto.setHexCode(pigment.getHexCode());
                    dto.setProportion(proportion);
                    result.add(dto);
                });
            }
        }
        System.out.println("Pigmentos encontrados para cor ID " + colorId + ": " + result.size());
        return result;
    }

    @Transactional
    public void updateColorCompositions(UUID colorId, List<PigmentCompositionDTO> updatedPigments) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new EntityNotFoundException("Cor não encontrada"));

        for (Composition composition : color.getCompositions()) {
            Map<Pigment, Double> newProportions = new HashMap<>();

            for (PigmentCompositionDTO dto : updatedPigments) {
                Optional<Pigment> existingPigmentOpt = pigmentRepository.findByNameIgnoreCase(dto.getName());

                Pigment pigment;
                if (existingPigmentOpt.isPresent()) {
                    pigment = existingPigmentOpt.get();
                    pigment.setName(dto.getName());
                    pigment.setHexCode(dto.getHexCode());
                } else {
                    pigment = new Pigment();
                    pigment.setName(dto.getName());
                    pigment.setHexCode(dto.getHexCode());
                    pigmentRepository.save(pigment);
                }

                newProportions.put(pigment, dto.getProportion());
            }

            composition.setPigmentProportions(newProportions);
        }

        colorRepository.save(color);
    }



    public List<PigmentCompositionDTO> getPigmentsByColorName(String colorName) {
        Optional<Color> optionalColor = colorRepository.findByNameIgnoreCase(colorName);
        if (optionalColor.isEmpty()) {
            return List.of();
        }

        Color color = optionalColor.get();
        List<PigmentCompositionDTO> result = new ArrayList<>();

        for (Composition composition : color.getCompositions()) {
            if (composition.getPigmentProportions() != null) {
                composition.getPigmentProportions().forEach((pigment, proportion) -> {
                    result.add(new PigmentCompositionDTO(
                            pigment.getName(),
                            pigment.getHexCode(),
                            proportion
                    ));
                });
            }
        }
        System.out.println("Pigmentos encontrados para cor " + colorName + ": " + result.size());
        return result;
    }

    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    public Color createColor(ColorCreateDTO dto) {
        Color color = new Color();
        color.setName(dto.getName());
        color.setBrand(dto.getBrand());
        color.setColorCode(dto.getColorCode());
        color.setHexCode(dto.getHexCode());

        List<Composition> compositions = new ArrayList<>();

        if (dto.getCompositions() != null) {
            for (CompositionDTO compDto : dto.getCompositions()) {
                Composition composition = new Composition();
                composition.setPercentage(compDto.getPercentage());
                composition.setColor(color);

                Map<Pigment, Double> pigmentProportions = new HashMap<>();
                List<Pigment> pigmentList = new ArrayList<>();

                for (Map.Entry<UUID, Double> entry : compDto.getPigmentProportions().entrySet()) {
                    UUID pigmentId = entry.getKey();
                    Double proportion = entry.getValue();

                    Pigment pigment = pigmentRepository.findById(pigmentId)
                            .orElseThrow(() -> new RuntimeException("Pigment not found: " + pigmentId));

                    pigmentProportions.put(pigment, proportion);
                    pigmentList.add(pigment);
                }
                composition.setPigments(pigmentList);
                composition.setPigmentProportions(pigmentProportions);

                compositions.add(composition);
            }
        }

        color.setCompositions(compositions);
        return colorRepository.save(color);
    }

    public ColorDTO updateColor(UUID id, ColorUpdateDTO updateDTO) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found"));

        if (updateDTO.getName() != null) {
            color.setName(updateDTO.getName());
        }
        if (updateDTO.getBrand() != null) {
            color.setBrand(updateDTO.getBrand());
        }
        if (updateDTO.getColorCode() != null) {
            color.setColorCode(updateDTO.getColorCode());
        }
        if (updateDTO.getHexCode() != null) {
            color.setHexCode(updateDTO.getHexCode());
        }

        if (updateDTO.getCompositions() != null) {
            color.getCompositions().clear();

            for (CompositionDTO compDto : updateDTO.getCompositions()) {
                Composition composition = new Composition();
                composition.setPercentage(compDto.getPercentage());
                composition.setColor(color);

                Map<Pigment, Double> pigmentProportions = new HashMap<>();
                List<Pigment> pigmentList = new ArrayList<>();

                if (compDto.getPigmentProportions() != null) {
                    for (Map.Entry<UUID, Double> entry : compDto.getPigmentProportions().entrySet()) {
                        UUID pigmentId = entry.getKey();
                        Double proportion = entry.getValue();

                        Pigment pigment = pigmentRepository.findById(pigmentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pigment not found: " + pigmentId));

                        pigmentProportions.put(pigment, proportion);
                        pigmentList.add(pigment);
                    }
                }

                composition.setPigments(pigmentList);
                composition.setPigmentProportions(pigmentProportions);
                color.getCompositions().add(composition);
            }
        }

        Color savedColor = colorRepository.save(color);
        return new ColorDTO(savedColor);
    }


    public void deleteColor(UUID id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found"));

        colorRepository.delete(color);
    }
}