package com.example.paintlab.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SavedColorDTO {
    private UUID id;
    private String colorCode;
    private String colorName;
    private String environmentTag;
    private String notes;
    private LocalDateTime savedDate;
    private String hexCode;

    public SavedColorDTO() {}

    public SavedColorDTO(String colorCode, String colorName, String environmentTag) {
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.environmentTag = environmentTag;
    }

    public SavedColorDTO(String colorCode, String colorName, String environmentTag, String notes) {
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.environmentTag = environmentTag;
        this.notes = notes;
    }
}