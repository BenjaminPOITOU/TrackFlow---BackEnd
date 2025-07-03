package com.eql.cda.track.flow.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Represents an annotation made on a specific version of a track.
 * An annotation has content, a time position, a category, and a status.
 */
@Entity
@Table(name = "annotations")
@EntityListeners(AuditingEntityListener.class)
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Float timePosition;

    @CreatedDate
    @Column(updatable = false)
    private Instant creationDate;

    private Instant supressionDate;
    private boolean isResolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id")
    private Version version;

    @Enumerated(EnumType.STRING)
    private AnnotationCategory annotationCategory;

    @Enumerated(EnumType.STRING)
    private AnnotationStatus annotationStatus;

    /**
     * Default constructor required by the persistence framework (JPA).
     */
    public Annotation() {
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

    public Instant getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Instant getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(Instant supressionDate) {
        this.supressionDate = supressionDate;
    }

    public boolean isResolved() {
        return isResolved;
    }
    public void setResolved(boolean resolved) {
        this.isResolved = resolved;
    }

    public Version getVersion() {
        return version;
    }
    public void setVersion(Version version) {
        this.version = version;
    }

    public AnnotationCategory getAnnotationCategory() {
        return annotationCategory;
    }
    public void setAnnotationCategory(AnnotationCategory annotationCategory) {
        this.annotationCategory = annotationCategory;
    }

    public AnnotationStatus getAnnotationStatus() {
        return annotationStatus;
    }
    public void setAnnotationStatus(AnnotationStatus annotationStatus) {
        this.annotationStatus = annotationStatus;
    }
}