package com.example.paintlab.domain.savedcolor;

import com.example.paintlab.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "saved_colors")
@Data
public class SavedColor {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("savedColors")
    private User user;

    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @Column(name = "color_name", nullable = false)
    private String colorName;

    @Column(name = "environment_tag", nullable = false)
    private String environmentTag;

    @Column(name = "notes")
    private String notes;

    @Column(name = "saved_date")
    private LocalDateTime savedDate;

    public SavedColor() {}

    public SavedColor(User user, String colorCode, String colorName, String environmentTag) {
        this.user = user;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.environmentTag = environmentTag;
        this.savedDate = LocalDateTime.now();
    }

    public SavedColor(User user, String colorCode, String colorName, String environmentTag, String notes) {
        this.user = user;
        this.colorCode = colorCode;
        this.colorName = colorName;
        this.environmentTag = environmentTag;
        this.notes = notes;
        this.savedDate = LocalDateTime.now();
    }
}