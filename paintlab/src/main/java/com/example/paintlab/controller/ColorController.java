package com.example.paintlab.controller;

import com.example.paintlab.dto.color.ColorCreateDTO;
import com.example.paintlab.dto.color.ColorDTO;
import com.example.paintlab.dto.color.ColorUpdateDTO;
import com.example.paintlab.dto.PigmentCompositionDTO;
import com.example.paintlab.domain.color.Color;
import com.example.paintlab.service.ColorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/colors")
public class ColorController {

    private final ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Color>> searchColors(@RequestParam String q) {
        List<Color> colors = colorService.searchColors(q);
        return ResponseEntity.ok(colors);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable UUID id) {
        Color color = colorService.getColorById(id);
        return ResponseEntity.ok(color);
    }

    @GetMapping("/{id}/pigments")
    public ResponseEntity<List<PigmentCompositionDTO>> getPigmentsByColorId(@PathVariable UUID id) {
        List<PigmentCompositionDTO> pigments = colorService.getPigmentsByColorId(id);
        if (pigments.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pigments);
    }

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        List<Color> colors = colorService.getAllColors();
        return ResponseEntity.ok(colors);
    }

    @PostMapping
    public ResponseEntity<Color> createColor(@RequestBody ColorCreateDTO dto){
        Color createdColor = colorService.createColor(dto);
        return ResponseEntity.ok(createdColor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColorDTO> updateColor (@PathVariable UUID id, @RequestBody ColorUpdateDTO updateDTO){
        ColorDTO updatedColor = colorService.updateColor(id, updateDTO);
        return ResponseEntity.ok(updatedColor);
    }

    @PutMapping("/{colorId}/compositions")
    public ResponseEntity<?> updateColorCompositions(
            @PathVariable UUID colorId,
            @RequestBody List<PigmentCompositionDTO> updatedPigments) {

        try {
            colorService.updateColorCompositions(colorId, updatedPigments);
            return ResponseEntity.ok("Composição atualizada com sucesso!");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar composição.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColor(@PathVariable UUID id) {
        colorService.deleteColor(id);
        return ResponseEntity.noContent().build();
    }

}