package com.eql.cda.track.flow.controller;




import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationResponseDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.UserRecentAnnotationDto;
import com.eql.cda.track.flow.service.AnnotationService;
import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;


// TODO: Ajouter la sécurité (@PreAuthorize) pour vérifier que l'utilisateur a le droit d'agir sur ces annotations (via la Version/Composition/Projet)

@RestController
@RequestMapping("/api") // Base commune
public class AnnotationController {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationController.class);

    private final AnnotationService annotationService;

    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
     * Crée une nouvelle annotation associée à une version spécifique.
     * L'ID de la version est passé dans l'URL.
     *
     * @param versionId L'ID de la version parente.
     * @param createDto Le DTO contenant les détails de l'annotation à créer.
     * @return ResponseEntity contenant l'AnnotationResponseDto créé (201) ou un statut d'erreur.
     */
    @PostMapping("/versions/{versionId}/annotations")
    public ResponseEntity<?> createAnnotation(@PathVariable Long versionId,
                                              @Valid @RequestBody AnnotationCreateDto createDto) {
        logger.info("POST /api/versions/{}/annotations - Request to create annotation.", versionId);
        logger.debug("Request body: {}", createDto);
        try {
            // Le service gère la liaison avec la versionId
            AnnotationResponseDto createdAnnotation = annotationService.createAnnotation(createDto, versionId);
            logger.info("Annotation created with ID: {} for version {}", createdAnnotation.id(), versionId);

            // Retourner 201 Created avec le corps
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnnotation);

        } catch (EntityNotFoundException e) {
            // Si la version parente n'est pas trouvée
            logger.warn("Annotation creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (IllegalArgumentException e) {
            // Erreur de validation métier potentielle du service
            logger.warn("Annotation creation failed: Invalid argument - {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        }
        // TODO: Gérer les erreurs de validation de @Valid plus proprement avec BindingResult ou @ControllerAdvice
        catch (Exception e) {
            logger.error("Annotation creation failed for version {}: Unexpected error - {}", versionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred creating the annotation.");
        }
    }
    @GetMapping("/annotations/recent")
    public ResponseEntity<Page<UserRecentAnnotationDto>> getMyRecentAnnotations(

            @RequestParam String username,
            @PageableDefault(size = 15, sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable) {

        // Le 'username' vient maintenant du paramètre @RequestParam
        logger.info("GET /api/annotations/recent - Request for user '{}' (from request param), pageable: {}", username, pageable);

        try {
            // --- Appel du service avec le username du paramètre ---
            Page<UserRecentAnnotationDto> recentAnnotations = annotationService.findRecentAnnotationsForUser(username, pageable);
            return ResponseEntity.ok(recentAnnotations);

        } catch (UsernameNotFoundException e) {
            // Le service peut toujours lever cette exception si le username fourni n'existe pas en BDD
            logger.warn("User not found for username provided in parameter: '{}'", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 est plus logique ici
        } catch (Exception e) {
            logger.error("Error retrieving recent annotations for user '{}': {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /**
     * Récupère une annotation spécifique par son ID.
     *
     * @param annotationId L'ID de l'annotation à récupérer.
     * @return ResponseEntity contenant l'AnnotationResponseDto (200) ou 404.
     */
    @GetMapping("/annotations/{annotationId}")
    public ResponseEntity<AnnotationResponseDto> getAnnotationById(@PathVariable Long annotationId) {
        logger.info("GET /api/annotations/{} - Request to retrieve annotation.", annotationId);
        try {
            AnnotationResponseDto annotationDto = annotationService.getAnnotationById(annotationId);
            return ResponseEntity.ok(annotationDto);
        } catch (EntityNotFoundException e) {
            logger.warn("Annotation {} not found.", annotationId);
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            logger.error("Error retrieving annotation {}: {}", annotationId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }

    /**
     * Récupère toutes les annotations associées à une version spécifique.
     *
     * @param versionId L'ID de la version dont on veut les annotations.
     * @return ResponseEntity contenant la liste des AnnotationResponseDto (200) ou une liste vide/erreur.
     */
    @GetMapping("/versions/{versionId}/annotations")
    public ResponseEntity<List<AnnotationResponseDto>> getAllAnnotationsByVersionId(
            @PathVariable Long versionId) {
        logger.info("GET /api/versions/{}/annotations - Request to list ALL annotations", versionId);
        try {
            // --- MODIFICATION : Appel à une nouvelle méthode de service ---
            // On garde un tri implicite (par date de création desc) pour la cohérence
            List<AnnotationResponseDto> annotations = annotationService.getAllAnnotationsByVersionIdSorted(versionId);
            // --- FIN MODIFICATION ---

            return ResponseEntity.ok(annotations);

        } catch (EntityNotFoundException e) { // Si le service lève ça quand la version n'existe pas
            logger.warn("Cannot list annotations: Version {} not found.", versionId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving all annotations for version {}: {}", versionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    /**
     * Met à jour une annotation existante.
     * Utilise PATCH pour les mises à jour partielles (champs null dans le DTO sont ignorés).
     *
     * @param annotationId L'ID de l'annotation à mettre à jour.
     * @param updateDto Le DTO contenant les champs à modifier.
     * @return ResponseEntity contenant l'AnnotationResponseDto mise à jour (200) ou un statut d'erreur.
     */
    @PatchMapping("/annotations/{annotationId}")
    public ResponseEntity<?> updateAnnotation(@PathVariable Long annotationId,
                                              @Valid @RequestBody AnnotationUpdateDto updateDto) {
        logger.info("PATCH /api/annotations/{} - Request to update annotation.", annotationId);
        logger.debug("Request body: {}", updateDto); // Peut être null ou partiel
        try {
            AnnotationResponseDto updatedAnnotation = annotationService.updateAnnotation(annotationId, updateDto);
            logger.info("Annotation {} updated successfully.", annotationId);
            return ResponseEntity.ok(updatedAnnotation);
        } catch (EntityNotFoundException e) {
            logger.warn("Annotation update failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // 404
        } catch (IllegalArgumentException e) {
            logger.warn("Annotation update failed: Invalid argument - {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        }
        // TODO: Gérer validation @Valid
        catch (Exception e) {
            logger.error("Annotation update failed for ID {}: Unexpected error - {}", annotationId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred updating the annotation."); // 500
        }
    }

    /**
     * Supprime (soft delete) une annotation spécifique par son ID.
     *
     * @param annotationId L'ID de l'annotation à supprimer.
     * @return ResponseEntity avec statut 204 (No Content) ou un statut d'erreur.
     */
    @DeleteMapping("/annotations/{annotationId}")
    public ResponseEntity<Void> deleteAnnotation(@PathVariable Long annotationId) {
        logger.info("DELETE /api/annotations/{} - Request to delete annotation.", annotationId);
        try {
            annotationService.deleteAnnotation(annotationId);
            logger.info("Annotation {} marked for deletion successfully.", annotationId);
            return ResponseEntity.noContent().build(); // 204
        } catch (EntityNotFoundException e) {
            logger.warn("Annotation deletion failed: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404
        } catch (Exception e) {
            logger.error("Annotation deletion failed for ID {}: Unexpected error - {}", annotationId, e.getMessage(), e);
            // Ne pas retourner de corps pour une erreur sur DELETE idéalement, juste le statut
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500
        }
    }
}