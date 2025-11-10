package com.example.paintlab.domain.history;

import com.example.paintlab.domain.user.User;
import com.example.paintlab.domain.color.Color;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history")
@Data
public class History {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("histories")
    private User user;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    @Column(name = "consultation_date")
    private LocalDateTime consultationDate;

    public History() {}

    public History(User user, Color color) {
        this.user = user;
        this.color = color;
        this.consultationDate = LocalDateTime.now();
    }
}