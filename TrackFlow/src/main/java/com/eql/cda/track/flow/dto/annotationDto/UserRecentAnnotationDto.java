package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;

import java.time.Instant;

public record UserRecentAnnotationDto(
        Long annotationId,
      String content, Float timePosition, AnnotationStatus status,
      AnnotationCategory category,
      Instant creationDate,
      Long versionId,
      String versionName,
      Long compositionId,
      String compositionName
) {}
