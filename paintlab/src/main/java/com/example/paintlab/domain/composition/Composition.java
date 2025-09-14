package com.example.paintlab.domain.composition;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.pigments.Pigment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Table(name = "composition")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Double porcentagem;

    @ManyToOne
    @JoinColumn(name = "id_cor", nullable = false)
    private Color color;

    @ManyToMany
    @JoinTable(
            name = "composition_pigments",
            joinColumns = @JoinColumn(name = "composition_id"),
            inverseJoinColumns = @JoinColumn(name = "pigment_id")
    )
    private List<Pigment> pigments = new ArrayList<>();
}
