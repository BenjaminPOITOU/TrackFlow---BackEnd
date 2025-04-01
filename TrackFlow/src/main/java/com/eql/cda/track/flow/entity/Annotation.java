package com.eql.cda.track.flow.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "annotations")
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private Float timePosition;
    private Date creationDate;
    private Date supressionDate;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;


    @Enumerated(EnumType.STRING)
    private AnnotationCategory annotationCategory;

    @Enumerated(EnumType.STRING)
    private AnnotationStatus annotationStatus;

    public Annotation() {
    }
    public Annotation(Long id, String content, Float timePosition, Date creationDate, Date supressionDate, Version version, AnnotationCategory annotationCategory, AnnotationStatus annotationStatus) {
        this.id = id;
        this.content = content;
        this.timePosition = timePosition;
        this.creationDate = creationDate;
        this.supressionDate = supressionDate;
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

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(Date supressionDate) {
        this.supressionDate = supressionDate;
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
