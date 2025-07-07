package com.eql.cda.track.flow.controller;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;
import com.eql.cda.track.flow.service.AnnotationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing annotations within the API hierarchy.
 */
@RestController
@RequestMapping("/api/")
public class AnnotationController {

    private final AnnotationService annotationService;

    /**
     * Constructs an AnnotationController with the required AnnotationService.
     *
     * @param annotationService the service to handle business logic for annotations.
     */
    @Autowired
    public AnnotationController(AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

    /**
     * Creates a new annotation associated with a specific version.
     *
     * @param versionId the ID of the parent version.
     * @param createDto the DTO containing the data for the new annotation.
     * @return a ResponseEntity with the created annotation view and HTTP status 201 (Created).
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the parent version does not exist.
     */
    @PostMapping("/versions/{versionId}/annotations")
    public ResponseEntity<AnnotationViewDto> createAnnotation(
            @PathVariable Long versionId,
            @Valid @RequestBody AnnotationCreateDto createDto) {
        AnnotationViewDto createdAnnotation = annotationService.createAnnotation(versionId, createDto);
        return new ResponseEntity<>(createdAnnotation, HttpStatus.CREATED);
    }

    /**
     * Retrieves all annotations for a specific version.
     *
     * @param versionId the ID of the parent version.
     * @return a ResponseEntity with a list of annotation summaries and HTTP status 200 (OK).
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the parent version does not exist.
     */
    @GetMapping("/versions/{versionId}/annotations")
    public ResponseEntity<List<AnnotationViewDto>> getAnnotationsForVersion(@PathVariable Long versionId) {
        List<AnnotationViewDto> annotations = annotationService.findAllAnnotationsByVersionId(versionId);
        return ResponseEntity.ok(annotations);
    }

    /**
     * Updates an existing annotation.
     *
     * @param annotationId the ID of the annotation to update.
     * @param updateDto    the DTO containing the fields to update.
     * @return a ResponseEntity with the updated annotation view and HTTP status 200 (OK).
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the annotation does not exist.
     */
    @PatchMapping("/annotations/{annotationId}")
    public ResponseEntity<AnnotationViewDto> updateAnnotation(
            @PathVariable Long annotationId,
            @Valid @RequestBody AnnotationUpdateDto updateDto) {
        AnnotationViewDto updatedAnnotation = annotationService.updateAnnotation(annotationId, updateDto);
        return ResponseEntity.ok(updatedAnnotation);
    }

    /**
     * Deletes an annotation. This is a soft delete.
     *
     * @param annotationId the ID of the annotation to delete.
     * @return a ResponseEntity with HTTP status 204 (No Content).
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the annotation does not exist.
     */
    @DeleteMapping("/annotations/{annotationId}")
    public ResponseEntity<Void> deleteAnnotation(@PathVariable Long annotationId) {
        annotationService.deleteAnnotation(annotationId);
        return ResponseEntity.noContent().build();
    }
}