package com.example.paintlab.domain.composition;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.pigments.Pigment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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

    private Double percentage;

    @ManyToOne
    @JoinColumn(name = "id_color", nullable = false)
    @JsonIgnore
    private Color color;

    @ManyToMany
    @JoinTable(
            name = "composition_pigments",
            joinColumns = @JoinColumn(name = "composition_id"),
            inverseJoinColumns = @JoinColumn(name = "pigment_id")
    )
    private List<Pigment> pigments = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "composition_pigment_proportions", joinColumns = @JoinColumn(name = "composition_id"))
    @MapKeyJoinColumn(name = "pigment_id")
    @Column(name = "proportion")
    @JsonIgnore
    private Map<Pigment, Double> pigmentProportions = new HashMap<>();
}
