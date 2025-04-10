package com.eql.cda.track.flow.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.Instant;


@Entity
@Table(name="accesses")
public class Access {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant creationDate;
    private Instant endOfPermission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @Enumerated(EnumType.STRING)
    @Column(name = "annotations_Right", nullable = false)
    private AnnotationRight annotationRight;

    public Access() {
    }

    public Access(Long id, Instant creationDate, Instant endOfPermission, User recipient, Version version, AnnotationRight annotationRight) {
        this.id = id;
        this.creationDate = creationDate;
        this.endOfPermission = endOfPermission;
        this.recipient = recipient;
        this.version = version;
        this.annotationRight = annotationRight;
    }

    public Long getId() {
        return id;
    }
    public Instant getCreationDate() {
        return creationDate;
    }
    public Instant getEndOfPermission() {
        return endOfPermission;
    }
    public User getRecipient() {
        return recipient;
    }
    public Version getVersion() {
        return version;
    }
    public AnnotationRight getAnnotationRight() {
        return annotationRight;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }
    public void setEndOfPermission(Instant endOfPermission) {
        this.endOfPermission = endOfPermission;
    }
    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }
    public void setAnnotationRight(AnnotationRight annotationRight) {
        this.annotationRight = annotationRight;
    }
    public void setVersion(Version version) {
        this.version = version;
    }


}
