package com.example.paintlab.service;

import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.PigmentDTO;
import com.example.paintlab.repositories.PigmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class PigmentService {

    private final PigmentRepository pigmentRepository;

    public PigmentService(PigmentRepository pigmentRepository) {
        this.pigmentRepository = pigmentRepository;
        // ✅ Garantir que os pigmentos básicos existam ao iniciar
        initializeBasicPigments();
    }

    public List<Pigment> getAllPigments() {
        return pigmentRepository.findAll();
    }

    public Pigment getPigmentById(UUID id) {
        return pigmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pigment not found with id: " + id));
    }

    public Pigment createPigment(PigmentDTO pigmentDTO) {
        Pigment pigment = new Pigment();
        pigment.setName(pigmentDTO.getName());
        // Se precisar de hexCode, ajuste conforme sua necessidade
        return pigmentRepository.save(pigment);
    }

    public Pigment updatePigment(UUID id, PigmentDTO pigmentDTO) {
        Pigment pigment = pigmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pigment not found"));

        if (pigmentDTO.getName() != null) {
            pigment.setName(pigmentDTO.getName());
        }

        return pigmentRepository.save(pigment);
    }

    public void deletePigment(UUID id) {
        Pigment pigment = pigmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pigment not found"));

        pigmentRepository.delete(pigment);
    }

    // ✅ INICIALIZAR APENAS OS PIGMENTOS BÁSICOS
    private void initializeBasicPigments() {
        if (pigmentRepository.count() == 0) {
            List<Pigment> basicPigments = List.of(
                    new Pigment("Azul", "#0000FF"),
                    new Pigment("Amarelo", "#FFFF00"),
                    new Pigment("Verde", "#008000"),
                    new Pigment("Marrom", "#8B4513"),
                    new Pigment("Ocre", "#CC7722"),
                    new Pigment("Preto", "#000000"),
                    new Pigment("Branco", "#FFFFFF"),
                    new Pigment("Violeta", "#8A2BE2"),
                    new Pigment("Vermelho", "#FF0000"),
                    new Pigment("Laranja", "#FFA500")
            );

            pigmentRepository.saveAll(basicPigments);
            System.out.println("✅ Pigmentos básicos criados: " + basicPigments.size());
        }
    }
}