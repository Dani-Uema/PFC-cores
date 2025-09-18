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

        // Criar pigmentos básicos
        Pigment azul = new Pigment(); azul.setName("Azul"); azul.setHexCode("#0000FF");
        Pigment preto = new Pigment(); preto.setName("Preto"); preto.setHexCode("#000000");
        Pigment vermelho = new Pigment(); vermelho.setName("Vermelho"); vermelho.setHexCode("#FF0000");
        Pigment branco = new Pigment(); branco.setName("Branco"); branco.setHexCode("#FFFFFF");
        Pigment ocre = new Pigment(); ocre.setName("Ocre"); ocre.setHexCode("#CC7722");
        Pigment violeta = new Pigment(); violeta.setName("Violeta"); violeta.setHexCode("#8F00FF");
        Pigment laranja = new Pigment(); laranja.setName("Laranja"); laranja.setHexCode("#FFA500");
        Pigment verde = new Pigment(); verde.setName("Verde"); verde.setHexCode("#008000");
        Pigment amarelo = new Pigment(); amarelo.setName("Amarelo"); amarelo.setHexCode("#FFFF00");
        Pigment marrom = new Pigment(); marrom.setName("Marrom"); marrom.setHexCode("#A52A2A");

        pigmentRepository.saveAll(List.of(
                azul, preto, vermelho, branco, ocre, violeta, laranja, verde, amarelo, marrom
        ));

        // Criar cor de exemplo e salvar primeiro
        Color papelPicado = new Color();
        papelPicado.setName("Papel Picado");
        papelPicado.setBrand("Suvinil");
        papelPicado.setColorCode("PP123");
        papelPicado.setHexCode("#F5E6D3");

        colorRepository.save(papelPicado); // ✅ salvar antes da Composition

        //Criar composição da cor
        Composition composition = new Composition();
        composition.setColor(papelPicado); // Color já existe no banco
        composition.setPercentage(100.0);
        composition.setPigments(List.of(vermelho, branco, amarelo));

        // proporção de cada pigmento
        Map<Pigment, Double> proportions = new HashMap<>();
        proportions.put(vermelho, 0.3);
        proportions.put(branco, 0.5);
        proportions.put(amarelo, 0.2);
        composition.setPigmentProportions(proportions);

        compositionRepository.save(composition);

        // atualizar lista de compositions no Color (opcional)
        papelPicado.setCompositions(List.of(composition));
        colorRepository.save(papelPicado);

        System.out.println("Dados de pigmentos e cor de exemplo carregados com sucesso!");
    }
}
