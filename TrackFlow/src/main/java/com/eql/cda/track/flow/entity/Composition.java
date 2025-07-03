package com.eql.cda.track.flow.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a single musical composition within a project.
 * A composition is the core creative unit, containing various branches and versions.
 */
@Entity
@Table(name = "compositions")
@EntityListeners(AuditingEntityListener.class)
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer compositionOrder;
    private String illustration;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastUpdateDate;

    private Instant suppressionDate;
    private Instant definitivSupressionDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "composition_sub_genders", joinColumns = @JoinColumn(name = "composition_id"))
    @Column(name = "sub_gender")
    private List<String> subGenders = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference("project-composition")
    private Project project;

    @OneToMany(mappedBy = "composition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("composition-branch")
    private Set<Branch> branches = new HashSet<>();;

    @Enumerated(EnumType.STRING)
    @Column(name = "composition_status")
    private CompositionStatus compositionStatus;

    public Composition() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCompositionOrder() {
        return compositionOrder;
    }
    public void setCompositionOrder(Integer compositionOrder) {
        this.compositionOrder = compositionOrder;
    }

    public String getIllustration() {
        return illustration;
    }
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Instant getSuppressionDate() {
        return suppressionDate;
    }
    public void setSuppressionDate(Instant suppressionDate) {
        this.suppressionDate = suppressionDate;
    }

    public Instant getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Instant definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public List<String> getSubGenders() {
        return subGenders;
    }
    public void setSubGenders(List<String> subGenders) {
        this.subGenders = subGenders;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Branch> getBranches() {
        return branches;
    }
    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }

    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }
    public void setCompositionStatus(CompositionStatus compositionStatus) {
        this.compositionStatus = compositionStatus;
    }
}