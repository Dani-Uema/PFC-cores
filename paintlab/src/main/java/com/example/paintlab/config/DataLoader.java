package com.example.paintlab.config;

import com.example.paintlab.domain.color.Color;
import com.example.paintlab.domain.composition.Composition;
import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.repositories.ColorRepository;
import com.example.paintlab.repositories.CompositionRepository;
import com.example.paintlab.repositories.PigmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final ColorRepository colorRepository;
    private final PigmentRepository pigmentRepository;
    private final CompositionRepository compositionRepository;

    public DataLoader(ColorRepository colorRepository,
                      PigmentRepository pigmentRepository,
                      CompositionRepository compositionRepository) {
        this.colorRepository = colorRepository;
        this.pigmentRepository = pigmentRepository;
        this.compositionRepository = compositionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1️⃣ Criar pigmentos básicos
        if (pigmentRepository.count() == 0) {
            pigmentRepository.saveAll(List.of(
                    new Pigment("Azul", "#0000FF"),
                    new Pigment("Preto", "#000000"),
                    new Pigment("Vermelho", "#FF0000"),
                    new Pigment("Branco", "#FFFFFF"),
                    new Pigment("Ocre", "#CC7722"),
                    new Pigment("Violeta", "#8F00FF"),
                    new Pigment("Laranja", "#FFA500"),
                    new Pigment("Verde", "#008000"),
                    new Pigment("Amarelo", "#FFFF00"),
                    new Pigment("Marrom", "#A52A2A")
            ));
        }

        // 2️⃣ Criar cor + composição
        if (!colorRepository.existsByName("Papel Picado")) {
            Color papelPicado = new Color();
            papelPicado.setName("Papel Picado");
            papelPicado.setBrand("Suvinil");
            papelPicado.setColorCode("PP123");
            papelPicado.setHexCode("#F5E6D3");

            colorRepository.save(papelPicado);

            Pigment vermelho = pigmentRepository.findByName("Vermelho").orElseThrow();
            Pigment branco = pigmentRepository.findByName("Branco").orElseThrow();
            Pigment amarelo = pigmentRepository.findByName("Amarelo").orElseThrow();

            Composition composition = new Composition();
            composition.setColor(papelPicado);
            composition.setPercentage(100.0);
            composition.setPigments(List.of(vermelho, branco, amarelo));

            Map<Pigment, Double> proportions = new HashMap<>();
            proportions.put(vermelho, 0.3);
            proportions.put(branco, 0.5);
            proportions.put(amarelo, 0.2);
            composition.setPigmentProportions(proportions);

            compositionRepository.save(composition);

            papelPicado.setCompositions(List.of(composition));
            colorRepository.save(papelPicado);
        }

        System.out.println("✅ Dados carregados com sucesso!");
    }
}
