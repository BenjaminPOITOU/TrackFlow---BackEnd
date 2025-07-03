package com.eql.cda.track.flow.dto.annotationDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * A Data Transfer Object for creating a new Annotation.
 * The category and status are received as strings (enum names) to be parsed by the application.
 * Validation constraints are applied to ensure data integrity.
 */
public record AnnotationCreateDto(
        @NotBlank(message = "Content cannot be blank.")
        @Size(max = 1000, message = "Content must be less than 1000 characters.")
        String content,

        @PositiveOrZero(message = "Time position must be a positive value or zero.")
        Float timePosition,

        @NotBlank(message = "Category cannot be blank.")
        String category,

        @NotBlank(message = "Status cannot be blank.")
        String status
) {
}