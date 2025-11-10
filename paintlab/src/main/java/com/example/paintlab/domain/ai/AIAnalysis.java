package com.example.paintlab.domain.ai;

import com.example.paintlab.domain.user.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIAnalysis {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "hex_code", nullable = false)
    private String hexCode;

    @Type(JsonBinaryType.class)
    @Column(name = "pigments_data", columnDefinition = "jsonb")
    private String pigmentsData;

    @Column(name = "analysis_date")
    private LocalDateTime analysisDate = LocalDateTime.now();

    public AIAnalysis(User user, String hexCode) {
        this.user = user;
        this.hexCode = hexCode;
        this.analysisDate = LocalDateTime.now();
    }
}