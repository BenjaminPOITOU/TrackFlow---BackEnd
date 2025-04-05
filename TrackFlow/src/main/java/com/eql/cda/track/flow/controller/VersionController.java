package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.versionDto.AudioUploadResponseDto;
import com.eql.cda.track.flow.dto.versionDto.NewVersionModalDto;
// Importe le DTO de création qui contient les validations
import com.eql.cda.track.flow.dto.versionDto.VersionCreateDto;
// IMPORTANT: Prévoir un DTO pour les réponses afin de ne pas exposer l'entité
// import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.dto.versionDto.VersionDetailDto;
import com.eql.cda.track.flow.dto.versionDto.VersionResponseDto;
import com.eql.cda.track.flow.dto.versionDto.VersionSummaryDto;
import com.eql.cda.track.flow.entity.Version; // Temporaire, à remplacer par DTO
import com.eql.cda.track.flow.service.VersionNamingService;
import com.eql.cda.track.flow.service.VersionService;

import jakarta.validation.Valid; // Pour activer la validation du DTO
import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger; // Utiliser SLF4J (standard Spring Boot)
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException; // Potentielle exception de delete
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// TODO: Ajouter la sécurité (ex: @PreAuthorize) sur chaque méthode

@RestController
// On pourrait choisir une base plus spécifique, mais /api/versions est simple pour les opérations par ID
// Et /api/compositions/{compositionId}/versions pour celles liées à une composition
// Choisissons une approche mixte pour la clarté des exemples.
@RequestMapping("/api")
public class VersionController {

    private static final Logger logger = LoggerFactory.getLogger(VersionController.class);

    private final VersionService versionService;
    private final VersionNamingService versionNamingService;

    @Autowired
    public VersionController(VersionService versionService, VersionNamingService versionNamingService) {
        this.versionService = versionService;
        this.versionNamingService = versionNamingService;
    }

    // --- Endpoint pour l'Upload (Étape 1 Création) ---
    /**
     * Handles the upload of an audio file for a future version.
     * Extracts metadata and stores the file in GCS.
     * Requires the ID of the composition this version will belong to for path generation.
     *
     * @param compositionId The ID of the parent composition.
     * @param file The audio file being uploaded.
     * @return ResponseEntity containing the AudioUploadResponseDto with file URL and metadata, or an error status.
     */
    @PostMapping("/compositions/{compositionId}/versions/upload")
    public ResponseEntity<AudioUploadResponseDto> handleAudioUpload(
            @PathVariable Long compositionId,
            @RequestParam("file") MultipartFile file) {

        logger.info("POST /api/compositions/{}/versions/upload - Received file upload request: {}", compositionId, file.getOriginalFilename());

        if (file.isEmpty()) {
            logger.warn("Upload failed: File is empty.");
            // On pourrait renvoyer un DTO d'erreur spécifique
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        }

        try {
            // Construire le préfixe GCS basé sur la composition
            String desiredPathPrefix = String.format("compositions/%d/versions/", compositionId);
            logger.debug("Generated GCS path prefix: {}", desiredPathPrefix);

            AudioUploadResponseDto responseDto = versionService.uploadAudioAndExtractMetadata(file, desiredPathPrefix);
            logger.info("File uploaded and metadata extracted successfully for composition {}. URL: {}", compositionId, responseDto.getUrl());
            return ResponseEntity.ok(responseDto); // 200 OK

        } catch (IllegalArgumentException e) {
            logger.warn("Upload failed for composition {}: Invalid argument - {}", compositionId, e.getMessage());
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        } catch (IOException e) {
            logger.error("Upload failed for composition {}: IO Error during upload - {}", compositionId, e.getMessage(), e);
            // On pourrait vouloir une réponse plus détaillée
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        } catch (Exception e) {
            logger.error("Upload failed for composition {}: Unexpected error during metadata extraction or upload - {}", compositionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PostMapping("/compositions/{compositionId}/versions") // L'ID composition reste utile pour l'URL
    public ResponseEntity<VersionResponseDto> createVersion( // <-- Changement type retour
                                                             @PathVariable Long compositionId, // Gardé pour la sémantique URL et log peut-être
                                                             @Valid @RequestBody VersionCreateDto dto) {

        logger.info("POST /api/compositions/{}/versions - Received request to create version.", compositionId);
        logger.debug("Request body: {}", dto);

        try {
            // --- RETRAIT du compositionId de l'appel Service ---
            VersionResponseDto createdVersion = versionService.createVersion(compositionId, dto);
            // --- FIN RETRAIT ---

            logger.info("Version created successfully with ID: {} for composition {}", createdVersion.getId(), compositionId);

            // Construire l'URI avec l'ID de la nouvelle version
            // URI location = ... buildAndExpand(createdVersion.id()).toUri();

            // Retourner 201 Created avec le DTO dans le corps
            return ResponseEntity.status(HttpStatus.CREATED).body(createdVersion); // Type maintenant correct

        } catch (EntityNotFoundException e) { // ...
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Pas de corps sur 404?
        } catch (IllegalArgumentException e) { // ...
            return ResponseEntity.badRequest().body(null); // Pas de corps sur 400?
        } catch (IllegalStateException e) { // ...
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Pas de corps sur 409?
        } catch (Exception e) { // ...
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Pas de corps sur 500
        }
    }


    // --- Endpoint pour la Suppression ---
    /**
     * Deletes a specific version by its ID.
     * This includes deleting the associated file from GCS.
     *
     * @param versionId The ID of the version to delete.
     * @return ResponseEntity with status 204 No Content on success, or an error status.
     */
    @DeleteMapping("/versions/{versionId}")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long versionId) {
        logger.info("DELETE /api/versions/{} - Received request to delete version.", versionId);
        try {
            versionService.deleteVersion(versionId);
            logger.info("Version {} deleted successfully.", versionId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (EntityNotFoundException e) {
            logger.warn("Deletion failed: Version {} not found.", versionId);
            return ResponseEntity.notFound().build(); // 404 Not Found
        } catch (Exception e) {
            // Autres erreurs potentielles (DB, etc.)
            logger.error("Deletion failed for version {}: Unexpected error - {}", versionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 (Generic Error)
        }
    }

    // --- Endpoint pour Préparer la Modale de Création ---
    /**
     * Retrieves the necessary data to populate the 'new version' modal/form,
     * based on the context of an existing version.
     *
     * @param currentVersionId The ID of the version the user is currently viewing or branching from.
     * @return ResponseEntity containing the NewVersionModalDto or an error status.
     */
    @GetMapping("/versions/{currentVersionId}/prepare-new")
    public ResponseEntity<NewVersionModalDto> getNewVersionModalData(@PathVariable Long currentVersionId) {
        logger.info("GET /api/versions/{}/prepare-new - Preparing modal data.", currentVersionId);
        try {
            NewVersionModalDto modalData = versionService.prepareNewVersionModalData(currentVersionId);
            logger.debug("Modal data prepared successfully for version {}", currentVersionId);
            return ResponseEntity.ok(modalData);
        } catch (EntityNotFoundException e) {
            logger.warn("Failed to prepare modal data: Version {} not found.", currentVersionId);
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalStateException e) {
            logger.error("Failed to prepare modal data for version {}: Data inconsistency - {}", currentVersionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 (Inconsistent Data)
        } catch (Exception e) {
            logger.error("Failed to prepare modal data for version {}: Unexpected error - {}", currentVersionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 (Generic Error)
        }
    }

    // --- Endpoint pour Récupérer une Version par ID ---
    /**
     * Retrieves a specific version by its ID.
     *
     * @param versionId The ID of the version to retrieve.
     * @return ResponseEntity containing the Version (or VersionResponseDto) or 404 Not Found.
     */
    @GetMapping("/versions/{versionId}")
    public ResponseEntity<VersionDetailDto> getVersionById(@PathVariable Long versionId) {
        logger.info("GET /api/versions/{} - Request to retrieve version.", versionId);
        try {
            // Appel à la nouvelle méthode de service qui retourne le DTO
            VersionDetailDto versionDetails = versionService.getVersionDetailsById(versionId);
            return ResponseEntity.ok(versionDetails); // Retourne le DTO avec 200 OK

        } catch (EntityNotFoundException e) {
            logger.warn("Version {} not found.", versionId);
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            logger.error("Error retrieving version {}: {}", versionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }


    // --- Endpoint pour Lister les Versions (Exemple: Par Composition) ---
    /**
     * Retrieves a list of versions belonging to a specific composition.
     *
     * @param compositionId The ID of the parent composition.
     * @return ResponseEntity containing a list of versions (or VersionSummaryDtos).
     */
    @GetMapping("/compositions/{compositionId}/versions")
    public ResponseEntity<List<VersionSummaryDto>> getVersionsByComposition(
            @PathVariable Long compositionId,
            @RequestParam(value = "branchId", required = false) Long branchId) {

        logger.info("GET /api/compositions/{}/versions - Request to list versions. Filter branchId: {}",
                compositionId, branchId != null ? branchId : "None");

        try {
            // Optional.ofNullable pour gérer si le paramètre est absent
            List<VersionSummaryDto> versions = versionService.getVersionsForComposition(compositionId, Optional.ofNullable(branchId));
            return ResponseEntity.ok(versions);

        } catch (EntityNotFoundException e) {
            // Si la composition ou la branche (si fournie) n'est pas trouvée
            logger.warn("Cannot list versions: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404
        } catch (IllegalArgumentException e) {
            // Si la branche n'appartient pas à la composition
            logger.warn("Cannot list versions: {}", e.getMessage());
            return ResponseEntity.badRequest().build(); // 400
        } catch (Exception e) {
            logger.error("Error listing versions for composition {}: {}", compositionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }


}