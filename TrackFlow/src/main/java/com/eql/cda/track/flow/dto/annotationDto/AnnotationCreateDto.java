package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AnnotationCreateDto(

        @NotBlank
        @Size(max = 1000) // Ajuste la taille max
        String content,

        @PositiveOrZero // Ou accepte null si PositionOrZero n'accepte pas null directement
        Float timePosition, // Nullable

        @NotNull
        String category,

        @NotNull
        String status // Le frontend peut envoyer la valeur initiale)
) {}
