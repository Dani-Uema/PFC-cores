package com.example.paintlab.controller;

import com.example.paintlab.dto.SavedColorDTO;
import com.example.paintlab.service.SavedColorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/saved-colors")
public class SavedColorController {

    private final SavedColorService savedColorService;

    public SavedColorController(SavedColorService savedColorService) {
        this.savedColorService = savedColorService;
    }

    @PostMapping
    public ResponseEntity<?> saveColor(@RequestBody SavedColorDTO savedColorDTO,
                                       @RequestHeader("userId") String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            SavedColorDTO saved = savedColorService.saveColor(savedColorDTO, userUUID);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar cor: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getSavedColors(@RequestHeader("userId") String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            List<SavedColorDTO> savedColors = savedColorService.findByUserId(userUUID);
            return ResponseEntity.ok(savedColors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar cores salvas: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSavedColorsByTag(@RequestHeader("userId") String userId,
                                                 @RequestParam String tag) {
        try {
            UUID userUUID = UUID.fromString(userId);
            List<SavedColorDTO> savedColors = savedColorService.findByUserIdAndEnvironmentTag(userUUID, tag);
            return ResponseEntity.ok(savedColors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar cores por tag: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSavedColor(@PathVariable UUID id,
                                              @RequestHeader("userId") String userId) {
        try {
            UUID userUUID = UUID.fromString(userId);
            savedColorService.deleteSavedColor(id, userUUID);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deletar cor: " + e.getMessage());
        }
    }
}