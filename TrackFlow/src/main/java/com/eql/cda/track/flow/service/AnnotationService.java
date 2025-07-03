package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationUpdateDto;
import com.eql.cda.track.flow.dto.annotationDto.AnnotationViewDto;

import java.util.List;

/**
 * Service interface for managing {@link com.eql.cda.track.flow.entity.Annotation} entities.
 * Defines the business logic for annotation-related operations.
 */
public interface AnnotationService {

    /**
     * Creates a new annotation for a specific version.
     *
     * @param versionId the ID of the parent version.
     * @param createDto the DTO containing the data for the new annotation.
     * @return a DTO representing the newly created annotation.
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the version with the given ID does not exist.
     */
    AnnotationViewDto createAnnotation(Long versionId, AnnotationCreateDto createDto);

    /**
     * Retrieves a list of all annotations for a specific version.
     *
     * @param versionId the ID of the parent version.
     * @return a list of summary DTOs for the annotations, sorted by creation date.
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the version with the given ID does not exist.
     */
    List<AnnotationViewDto> findAllAnnotationsByVersionId(Long versionId);

    /**
     * Updates an existing annotation.
     *
     * @param annotationId the ID of the annotation to update.
     * @param updateDto    the DTO containing the fields to update.
     * @return a DTO representing the updated annotation.
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the annotation with the given ID does not exist.
     */
    AnnotationViewDto updateAnnotation(Long annotationId, AnnotationUpdateDto updateDto);

    /**
     * Deletes an annotation by its ID. This performs a soft delete.
     *
     * @param annotationId the ID of the annotation to delete.
     * @throws com.eql.cda.track.flow.exception.ResourceNotFoundException if the annotation with the given ID does not exist.
     */
    void deleteAnnotation(Long annotationId);
}