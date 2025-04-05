package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record NewVersionAnnotationDto(

        @NotBlank
        @Size(max = 1000) String content,

        @PositiveOrZero Float timePosition,
        @NotNull
        AnnotationCategory category,

        @NotNull
        AnnotationStatus status,
        Long sourceAnnotationId // ID de l'annotation N-1 source (nullable si création pure)

) {}
