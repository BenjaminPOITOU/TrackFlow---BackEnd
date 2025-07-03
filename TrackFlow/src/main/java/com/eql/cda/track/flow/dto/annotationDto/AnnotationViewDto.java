package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;

import java.time.Instant;

/**
 * A Data Transfer Object that provides a read-only representation of an {@link com.eql.cda.track.flow.entity.Annotation}.
 * It is designed to be nested within other DTOs, such as VersionViewDto.
 */
public class AnnotationViewDto {

    private Long id;
    private String content;
    private Float timePosition;
    private boolean isResolved;
    private Instant creationDate;
    private String annotationCategory;
    private String annotationStatus;

    /**
     * Default constructor required for framework instantiation.
     */
    public AnnotationViewDto() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Float getTimePosition() {
        return timePosition;
    }
    public void setTimePosition(Float timePosition) {
        this.timePosition = timePosition;
    }

    public boolean isResolved() {
        return isResolved;
    }
    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }

    public Instant getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public String getAnnotationCategory() {
        return annotationCategory;
    }
    public void setAnnotationCategory(String annotationCategory) {
        this.annotationCategory = annotationCategory;
    }

    public String getAnnotationStatus() {
        return annotationStatus;
    }
    public void setAnnotationStatus(String annotationStatus) {
        this.annotationStatus = annotationStatus;
    }
}