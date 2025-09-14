package com.example.paintlab.domain.pigments;

import com.example.paintlab.domain.composition.Composition;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pigments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Pigment {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String hexCode;

    @ManyToMany(mappedBy = "pigments")
    private List<Composition> compositions = new ArrayList<>();
}
