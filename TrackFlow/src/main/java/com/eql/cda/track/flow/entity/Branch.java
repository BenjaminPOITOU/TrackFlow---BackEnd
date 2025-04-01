package com.eql.cda.track.flow.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Date creationDate;
    private Date supressionDate;
    private Date definitivSupressionDate;
    private Date lastupdateDate;
    private Long branchParentId;

    @ManyToOne
    @JoinColumn(name = "composition_id")
    private Composition composition;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private Set<Version> versions;

    public Branch() {
    }
    public Branch(Long id, String name, String description, Date creationDate, Date supressionDate, Date definitivSupressionDate, Date lastupdateDate, Long branchParentId, Composition composition, Set<Version> versions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.lastupdateDate = lastupdateDate;
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

    public Date getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Date definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public Date getLastupdateDate() {
        return lastupdateDate;
    }
    public void setLastupdateDate(Date lastupdateDate) {
        this.lastupdateDate = lastupdateDate;
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
