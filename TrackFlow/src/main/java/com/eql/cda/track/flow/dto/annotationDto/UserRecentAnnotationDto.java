package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;

import java.time.LocalDateTime;

public record UserRecentAnnotationDto(
        Long annotationId,
      String content, Float timePosition, AnnotationStatus status,
      AnnotationCategory category,
      LocalDateTime creationDate,
      Long versionId,
      String versionName,
      Long compositionId,
      String compositionName
) {}
