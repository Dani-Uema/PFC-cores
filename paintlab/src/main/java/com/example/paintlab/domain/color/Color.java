package com.example.paintlab.domain.color;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String colorCode;
    private  String hexCode;

//    @OneToMany(mappedBy = "colors", cascade = CascadeType.ALL, orphanRemoval= true)
//    private List<composition> compositions = new ArrayList<>();

}
