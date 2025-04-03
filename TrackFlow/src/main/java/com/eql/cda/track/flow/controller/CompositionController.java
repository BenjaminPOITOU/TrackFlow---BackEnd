package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.service.CompositionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CompositionController {

    private final CompositionService compositionService;

    @Autowired
    public CompositionController(CompositionService compositionService) {
        this.compositionService = compositionService;
    }


    // --- CRUD Operations ---

    /**
     * Crée une nouvelle composition pour un projet spécifique.
     * POST /api/projects/{projectId}/compositions
     */
    @PostMapping("/projects/{projectId}/compositions")
    public ResponseEntity<CompositionViewDto> createComposition(
            @PathVariable Long projectId,
            @Valid @RequestBody CompositionCreateDto compositionCreateDto) {
        // !! SÉCURITÉ !! : Ici, il faudra vérifier que l'utilisateur authentifié
        // a le droit d'ajouter une composition au projet {projectId}.
        CompositionViewDto createdComposition = compositionService.createComposition(projectId, compositionCreateDto);
        return ResponseEntity.ok(createdComposition); // Statut 201 Created
    }

    /**
     * Récupère toutes les compositions (en résumé) pour un projet spécifique.
     * GET /api/projects/{projectId}/compositions
     */
    @GetMapping("/projects/{projectId}/compositions")
    public ResponseEntity<List<CompositionSummaryDto>> getAllCompositionsByProject(
            @PathVariable Long projectId) {
        // !! SÉCURITÉ !! : Ici, il faudra vérifier que l'utilisateur authentifié
        // a le droit de voir les compositions du projet {projectId}.
        List<CompositionSummaryDto> compositions = compositionService.getAllCompositions(projectId);
        return ResponseEntity.ok(compositions); // Statut 200 OK
    }

    /**
     * Récupère les détails d'une composition spécifique d'un projet.
     * GET /api/projects/{projectId}/compositions/{compositionId}
     */
    @GetMapping("/projects/{projectId}/compositions/{compositionId}")
    public ResponseEntity<CompositionViewDto> getCompositionById(
            @PathVariable Long projectId,
            @PathVariable Long compositionId) {
        // !! SÉCURITÉ !! : Vérifier que l'utilisateur authentifié a le droit
        // d'accéder à la composition {compositionId} du projet {projectId}.
        // Note: Le service ne prend que compositionId, projectId est pour le contexte/sécurité.
        try {
            CompositionViewDto composition = compositionService.getCurrentComposition(compositionId);
            // Optionnel : Vérifier que composition.getProjectId() == projectId si nécessaire
            return ResponseEntity.ok(composition); // Statut 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Statut 404 Not Found
        }
        // Ou utiliser un @ControllerAdvice pour gérer EntityNotFoundException globalement
    }

    /**
     * Met à jour partiellement une composition spécifique d'un projet.
     * PATCH /api/projects/{projectId}/compositions/{compositionId}
     */
    @PatchMapping("/projects/{projectId}/compositions/{compositionId}")
    public ResponseEntity<CompositionViewDto> updateComposition(
            @PathVariable Long projectId,
            @PathVariable Long compositionId,
            @Valid @RequestBody CompositionUpdateDto compositionUpdateDto) {
        // !! SÉCURITÉ !! : Vérifier que l'utilisateur authentifié a le droit
        // de modifier la composition {compositionId} du projet {projectId}.
        // Note: Le service ne prend que compositionId, projectId est pour le contexte/sécurité.
        try {
            CompositionViewDto updatedComposition = compositionService.updateComposition(compositionId, compositionUpdateDto);
            // Optionnel : Vérifier que updatedComposition.getProjectId() == projectId si nécessaire
            return ResponseEntity.ok(updatedComposition); // Statut 200 OK
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build(); // Statut 404 Not Found
        } catch (IllegalArgumentException e) { // Ex: titre vide
            return ResponseEntity.badRequest().body(null); // Ou un message d'erreur plus spécifique
        }
        // Ou utiliser un @ControllerAdvice
    }

    /**
     * Récupère les compositions récentes (en résumé) liées à un utilisateur spécifique.
     * GET /api/users/{userId}/compositions/recent
     * (Cohérent avec /api/users/{userId}/projects/recent)
     */
    @GetMapping("/users/{userId}/compositions/recent")
    public ResponseEntity<List<CompositionSummaryDto>> getRecentCompositionsByUser(
            @PathVariable Long userId) {
        // !! SÉCURITÉ !! : Vérifier que l'utilisateur authentifié est bien {userId}
        // OU a les droits (ex: admin) de voir les compositions récentes de {userId}.
        try {
            List<CompositionSummaryDto> recentCompositions = compositionService.getRecentSummaryCompositionDto(userId);
            return ResponseEntity.ok(recentCompositions); // Statut 200 OK
        } catch (EntityNotFoundException e) { // Si l'utilisateur n'existe pas
            return ResponseEntity.notFound().build(); // Statut 404 Not Found
        }

    }

    /**
     * Supprime une composition spécifique d'un projet.
     * DELETE /api/projects/{projectId}/compositions/{compositionId}
     */
    @DeleteMapping("/projects/{projectId}/compositions/{compositionId}")
    public ResponseEntity<Void> deleteComposition(
            @PathVariable Long projectId,
            @PathVariable Long compositionId) {
        // !! SÉCURITÉ !! : Vérifier que l'utilisateur authentifié a le droit
        // de supprimer la composition {compositionId} du projet {projectId}.
        // Note: Le service ne prend que compositionId, projectId est pour le contexte/sécurité.
        try {
            compositionService.deleteComposition(compositionId);
            return ResponseEntity.noContent().build(); // Statut 204 No Content
        } catch (EntityNotFoundException e) {
            // On peut choisir de renvoyer 404 ou 204 (si l'état final est "non existant")
            return ResponseEntity.notFound().build(); // Statut 404 Not Found
        }
        // Ou utiliser un @ControllerAdvice
    }


    // --- Endpoint Spécifique Utilisateur ---



}
