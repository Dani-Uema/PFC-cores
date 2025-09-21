package com.example.paintlab.domain.color;

import com.example.paintlab.domain.composition.Composition;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
@Table(name = "colors")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private String brand ;
    @Column(name = "color_code")
    private String colorCode;
    @Column(name = "hex_code")
    private  String hexCode;

    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Composition> compositions;
}
