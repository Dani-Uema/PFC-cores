package com.example.paintlab.domain.history;


import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "color_id", nullable = false)
    private Color color;

    private LocalDateTime consultationDate = LocalDateTime.now();
}
