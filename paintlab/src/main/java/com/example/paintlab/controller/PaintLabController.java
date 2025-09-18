package com.example.paintlab.controller;

import com.example.paintlab.dto.PigmentCompositionDTO;
import com.example.paintlab.domain.color.Color;
import com.example.paintlab.service.PaintLabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colors")
public class PaintLabController {

    private final PaintLabService paintLabService;

    public PaintLabController(PaintLabService paintLabService) {
        this.paintLabService = paintLabService;
    }

    //endpoint que retorna todos os pigmentos de uma cor
    @GetMapping("/{name}/pigments")
    public ResponseEntity<List<PigmentCompositionDTO>> getPigments(@PathVariable String name) {
        List<PigmentCompositionDTO> pigments = paintLabService.getPigmentsByColorName(name);
        if (pigments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pigments);
    }

    //endpoint para listar todas as cores
    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        List<Color> colors = paintLabService.getAllColors();
        return ResponseEntity.ok(colors);
    }
}

