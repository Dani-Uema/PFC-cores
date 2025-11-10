package com.example.paintlab.service;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.savedcolor.SavedColor;
import com.example.paintlab.domain.user.User;
import com.example.paintlab.dto.SavedColorDTO;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.SavedColorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SavedColorService {

    private final SavedColorRepository savedColorRepository;
    private final UserService userService;
    private final ColorRepository colorRepository;

    public SavedColorService(SavedColorRepository savedColorRepository, UserService userService, ColorRepository colorRepository) {
        this.savedColorRepository = savedColorRepository;
        this.userService = userService;
        this.colorRepository = colorRepository;
    }

    public SavedColorDTO saveColor(SavedColorDTO savedColorDTO, UUID userId) {
        User user = userService.findById(userId);

        List<SavedColor> existing = savedColorRepository.findByUserIdAndColorCodeAndEnvironmentTag(
                userId,
                savedColorDTO.getColorCode(),
                savedColorDTO.getEnvironmentTag()
        );

        if (!existing.isEmpty()) {
            throw new RuntimeException("Esta cor já foi salva com a tag '" + savedColorDTO.getEnvironmentTag() + "'");
        }

        SavedColor savedColor = new SavedColor();
        savedColor.setUser(user);
        savedColor.setColorCode(savedColorDTO.getColorCode());
        savedColor.setColorName(savedColorDTO.getColorName());
        savedColor.setEnvironmentTag(savedColorDTO.getEnvironmentTag());
        savedColor.setNotes(savedColorDTO.getNotes());

        SavedColor saved = savedColorRepository.save(savedColor);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<SavedColorDTO> findByUserId(UUID userId) {
        return savedColorRepository.findByUserIdOrderBySavedDateDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SavedColorDTO> findByUserIdAndEnvironmentTag(UUID userId, String tag) {
        return savedColorRepository.findByUserIdAndEnvironmentTagContaining(userId, tag)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteSavedColor(UUID savedColorId, UUID userId) {
        boolean exists = savedColorRepository.findByIdAndUserId(savedColorId, userId).isPresent();
        if (!exists) {
            throw new RuntimeException("Cor salva não encontrada ou não pertence ao usuário");
        }
        savedColorRepository.deleteByIdAndUserId(savedColorId, userId);
    }


    private SavedColorDTO convertToDTO(SavedColor savedColor) {
        SavedColorDTO dto = new SavedColorDTO();
        dto.setId(savedColor.getId());
        dto.setColorCode(savedColor.getColorCode());
        dto.setColorName(savedColor.getColorName());
        dto.setEnvironmentTag(savedColor.getEnvironmentTag());
        dto.setNotes(savedColor.getNotes());
        dto.setSavedDate(savedColor.getSavedDate());

        try {
            Color originalColor = colorRepository.findByColorCode(savedColor.getColorCode());
            if (originalColor != null && originalColor.getHexCode() != null) {
                dto.setHexCode(originalColor.getHexCode());
            } else {
                dto.setHexCode(generateColorFromName(savedColor.getColorName()));
            }
        } catch (Exception e) {
            dto.setHexCode(generateColorFromName(savedColor.getColorName()));
        }

        return dto;
    }

    private String generateColorFromName(String colorName) {
        if (colorName == null) return "#cccccc";

        int hash = colorName.hashCode();
        String hexColor = String.format("#%06x", hash & 0xFFFFFF);
        return hexColor;
    }

}