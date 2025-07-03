package com.eql.cda.track.flow.dto.annotationDto;

import jakarta.validation.constraints.Size;

/**
 * A Data Transfer Object for partially updating an existing Annotation.
 * All fields are optional. The category and status are received as strings.
 */
public record AnnotationUpdateDto(
        @Size(max = 1000, message = "Content must be less than 1000 characters.")
        String content,

        String category,

        String status
) {
}