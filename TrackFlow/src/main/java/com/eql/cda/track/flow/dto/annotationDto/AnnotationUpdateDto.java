package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AnnotationUpdateDto(

        @Size(max = 1000) // Validation s'applique si non-null
        String content,

        @PositiveOrZero
        Float timePosition,

        AnnotationCategory category,

        AnnotationStatus status
) {
}
