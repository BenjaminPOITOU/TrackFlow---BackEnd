package com.eql.cda.track.flow.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a branch of development within a musical composition.
 * A branch is a distinct line of work, which can have its own parent branch and contain multiple versions.
 * It is always associated with a single parent {@link Composition}.
 */
@Entity
@Table(name = "branches")
@EntityListeners(AuditingEntityListener.class)
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastUpdateDate;

    private Instant supressionDate;
    private Instant definitivSupressionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_branch_id")
    @JsonBackReference("branch-children")
    private Branch parent;

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference("branch-children")
    private Set<Branch> children = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "composition_id", nullable = false)
    @JsonBackReference("composition-branch")
    private Composition composition;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("branch-version")
    private Set<Version> versions = new HashSet<>();

    /**
     * Default constructor required by the persistence framework (JPA).
     */
    public Branch() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(Instant supressionDate) {
        this.supressionDate = supressionDate;
    }

    public Instant getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Instant definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public Instant getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Instant lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Branch getParent() {
        return parent;
    }
    public void setParent(Branch parent) {
        this.parent = parent;
    }

    public Set<Branch> getChildren() {
        return children;
    }
    public void setChildren(Set<Branch> children) {
        this.children = children;
    }

    public Composition getComposition() {
        return composition;
    }
    public void setComposition(Composition composition) {
        this.composition = composition;
    }

    public Set<Version> getVersions() {
        return versions;
    }
    public void setVersions(Set<Version> versions) {
        this.versions = versions;
    }
}