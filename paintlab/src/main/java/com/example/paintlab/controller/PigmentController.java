package com.example.paintlab.controller;

import com.example.paintlab.domain.pigments.Pigment;
import com.example.paintlab.dto.PigmentDTO;
import com.example.paintlab.service.PigmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pigments")
public class PigmentController {

    private final PigmentService pigmentService;

    public PigmentController(PigmentService pigmentService) {
        this.pigmentService = pigmentService;
    }

    @GetMapping
    public ResponseEntity<List<Pigment>> getAllPigments() {
        List<Pigment> pigments = pigmentService.getAllPigments();
        return ResponseEntity.ok(pigments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pigment> getPigmentById(@PathVariable UUID id) {
        Pigment pigment = pigmentService.getPigmentById(id);
        return ResponseEntity.ok(pigment);
    }

    @PostMapping
    public ResponseEntity<Pigment> createPigment(@RequestBody PigmentDTO pigmentDTO) {
        Pigment createdPigment = pigmentService.createPigment(pigmentDTO);
        return ResponseEntity.ok(createdPigment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pigment> updatePigment(@PathVariable UUID id, @RequestBody PigmentDTO pigmentDTO) {
        Pigment updatedPigment = pigmentService.updatePigment(id, pigmentDTO);
        return ResponseEntity.ok(updatedPigment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePigment(@PathVariable UUID id) {
        pigmentService.deletePigment(id);
        return ResponseEntity.noContent().build();
    }
}