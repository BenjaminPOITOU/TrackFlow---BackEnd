package com.eql.cda.track.flow.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "annotations")
@SQLDelete(sql = "UPDATE annotations SET supression_date = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "supression_date IS NULL")
@EntityListeners(AuditingEntityListener.class)
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Float timePosition;

    @CreatedDate
    private Instant creationDate;
    private Instant supressionDate;
    private boolean isResolved;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;


    @Enumerated(EnumType.STRING)
    private AnnotationCategory annotationCategory;

    @Enumerated(EnumType.STRING)
    private AnnotationStatus annotationStatus;

    public Annotation() {
    }

    public Annotation(Long id, String content, Float timePosition, Instant creationDate, Instant supressionDate, boolean isResolved, Version version, AnnotationCategory annotationCategory, AnnotationStatus annotationStatus) {
        this.id = id;
        this.content = content;
        this.timePosition = timePosition;
        this.creationDate = creationDate;
        this.supressionDate = supressionDate;
        this.isResolved = isResolved;
        this.version = version;
        this.annotationCategory = annotationCategory;
        this.annotationStatus = annotationStatus;
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

    public void setAnnotationCategory(AnnotationCategory annotationCategory) {
        this.annotationCategory = annotationCategory;
    }

    public void setAnnotationStatus(AnnotationStatus annotationStatus) {
        this.annotationStatus = annotationStatus;
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

    public AnnotationStatus getAnnotationStatus() {
        return annotationStatus;
    }



}
