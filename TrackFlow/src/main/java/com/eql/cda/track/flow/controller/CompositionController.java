package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.EnumDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionCreateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionSummaryDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionUpdateDto;
import com.eql.cda.track.flow.dto.compositionDto.CompositionViewDto;
import com.eql.cda.track.flow.entity.CompositionStatus;
import com.eql.cda.track.flow.entity.ProjectCommercialStatus;
import com.eql.cda.track.flow.entity.ProjectMusicalGenderPreDefined;
import com.eql.cda.track.flow.entity.ProjectPurpose;
import com.eql.cda.track.flow.entity.ProjectStatus;
import com.eql.cda.track.flow.entity.ProjectType;
import com.eql.cda.track.flow.entity.UserRole;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import com.eql.cda.track.flow.service.CompositionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CompositionController {

    private final CompositionService compositionService;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

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
    @GetMapping("/compositions/recent") // Nouvelle route standardisée
    public ResponseEntity<Page<CompositionSummaryDto>> getRecentCompositions(
            @RequestParam String username, // Pour l'instant
            @PageableDefault(size = 5, sort = "lastUpdateDate", direction = Sort.Direction.DESC) Pageable pageable) {

        logger.info("GET /api/compositions/recent - Request for user '{}', pageable: {}", username, pageable);

        try {
            Page<CompositionSummaryDto> recentCompositions = compositionService.findRecentCompositionsForUser(username, pageable);
            return ResponseEntity.ok(recentCompositions);
        } catch (UsernameNotFoundException e) {
            logger.warn("User not found for username: '{}'", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 si user inconnu
        } catch (Exception e) {
            logger.error("Error retrieving recent compositions for user '{}': {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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


    @RestController
    @RequestMapping("/api/enums") // Chemin de base pour toutes les énumérations
    public static class EnumController {

        // Méthode générique (interne) pour convertir un Enum en EnumDto
        private <E extends Enum<E>> EnumDto mapToEnumDto(E enumConstant, String label) {
            return new EnumDto(enumConstant.name(), label);
        }

        // --- Endpoints pour les Enums de Project ---

        @GetMapping("/project-commercial-status")
        public ResponseEntity<List<EnumDto>> getProjectCommercialStatus() {
            List<EnumDto> values = Arrays.stream(ProjectCommercialStatus.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        @GetMapping("/project-musical-gender")
        public ResponseEntity<List<EnumDto>> getProjectMusicalGenders() {
            List<EnumDto> values = Arrays.stream(ProjectMusicalGenderPreDefined.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        @GetMapping("/project-purpose")
        public ResponseEntity<List<EnumDto>> getProjectPurposes() {
            List<EnumDto> values = Arrays.stream(ProjectPurpose.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        @GetMapping("/project-status")
        public ResponseEntity<List<EnumDto>> getProjectStatus() {
            List<EnumDto> values = Arrays.stream(ProjectStatus.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        @GetMapping("/project-type")
        public ResponseEntity<List<EnumDto>> getProjectTypes() {
            List<EnumDto> values = Arrays.stream(ProjectType.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        // --- Endpoint pour l'Enum de Composition ---

        @GetMapping("/composition-status")
        public ResponseEntity<List<EnumDto>> getCompositionStatus() {
            List<EnumDto> values = Arrays.stream(CompositionStatus.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        // --- Endpoint pour l'Enum de User ---

        @GetMapping("/user-roles")
        public ResponseEntity<List<EnumDto>> getUserRoles() {
            List<EnumDto> values = Arrays.stream(UserRole.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }

        // --- Endpoint pour l'Enum de Version (Instruments) ---

        @GetMapping("/version-instruments")
        public ResponseEntity<List<EnumDto>> getVersionInstruments() {
            List<EnumDto> values = Arrays.stream(VersionInstrumentPreDefined.values())
                    .map(e -> mapToEnumDto(e, e.getLabel()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(values);
        }
    }
}
