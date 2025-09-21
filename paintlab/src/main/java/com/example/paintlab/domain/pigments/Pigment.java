package com.example.paintlab.domain.pigments;

import com.example.paintlab.domain.composition.Composition;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "hex_code")
    private String hexCode;

    @ManyToMany(mappedBy = "pigments")
    @JsonIgnore
    private List<Composition> compositions = new ArrayList<>();

    public Pigment(String name, String hexCode) {
        this.name = name;
        this.hexCode = hexCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pigment)) return false;
        Pigment pigment = (Pigment) o;
        return id != null && id.equals(pigment.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}


