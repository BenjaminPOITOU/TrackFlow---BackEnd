package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.dto.EnumDto;

import java.time.LocalDateTime;

public record AnnotationResponseDto(


        Long id,
        String content,
        Float timePosition, // Reste Float?, peut être null
        LocalDateTime creationDate,
        boolean resolved, // Directement calculé depuis le status
        EnumDto category, // Utilise EnumDto pour avoir { value: "MIXAGE", label: "Mixage" }
        EnumDto status,   // Utilise EnumDto pour avoir { value: "A_TRAITER", label: "À traiter" }
        Long versionId // L'ID de la version associée
) {

}
