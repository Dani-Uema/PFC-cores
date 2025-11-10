package com.example.paintlab.repositories;

import com.example.paintlab.domain.savedcolor.SavedColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SavedColorRepository extends JpaRepository<SavedColor, UUID> {

    List<SavedColor> findByUserIdOrderBySavedDateDesc(UUID userId);

    @Query("SELECT sc FROM SavedColor sc WHERE sc.user.id = :userId AND sc.colorCode = :colorCode AND sc.environmentTag = :environmentTag")
    List<SavedColor> findByUserIdAndColorCodeAndEnvironmentTag(
            @Param("userId") UUID userId,
            @Param("colorCode") String colorCode,
            @Param("environmentTag") String environmentTag
    );

    @Query("SELECT sc FROM SavedColor sc WHERE sc.user.id = :userId AND sc.environmentTag LIKE %:tag%")
    List<SavedColor> findByUserIdAndEnvironmentTagContaining(@Param("userId") UUID userId, @Param("tag") String tag);

    // CORREÇÃO: Adicionar método para verificar existência antes de deletar
    Optional<SavedColor> findByIdAndUserId(UUID id, UUID userId);

    // CORREÇÃO: Adicionar @Modifying e @Transactional na query de delete
    @Modifying
    @Query("DELETE FROM SavedColor sc WHERE sc.id = :id AND sc.user.id = :userId")
    void deleteByIdAndUserId(@Param("id") UUID id, @Param("userId") UUID userId);
}