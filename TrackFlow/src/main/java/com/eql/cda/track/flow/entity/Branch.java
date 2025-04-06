package com.eql.cda.track.flow.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

import java.time.Instant;
import java.util.Set;

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
    private Instant createdDate;
    private Instant  supressionDate;
    private Instant  definitivSupressionDate;

    @Column(name = "last_update_date")
    @LastModifiedDate
    private Instant lastUpdateDate;
    private Long branchParentId;

    @ManyToOne
    @JoinColumn(name = "composition_id")
    private Composition composition;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private Set<Version> versions;

    public Branch() {
    }
    public Branch(Long id, String name, String description, Instant createdDate, Instant supressionDate, Instant  definitivSupressionDate, Instant lastUpdateDate, Long branchParentId, Composition composition, Set<Version> versions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.lastUpdateDate = lastUpdateDate;
        this.branchParentId = branchParentId;
        this.composition = composition;
        this.versions = versions;
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

    public Long getBranchParentId() {
        return branchParentId;
    }
    public void setBranchParentId(Long branchParentId) {
        this.branchParentId = branchParentId;
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
