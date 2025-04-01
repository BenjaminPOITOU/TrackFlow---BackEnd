package com.eql.cda.track.flow.entity;

import jakarta.persistence.Column;
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
import java.util.Set;

@Entity
@Table(name = "compositions")
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer compositionOrder;
    private String illustration;
    private Integer numberOfVersions;
    private Integer numberOfBranches;
    private Date creationDate;
    private Date lastupdateDate;
    private Date suppressionDate;
    private Date definitivSupressionDate;
    private String subGender;
    private String projectMusicalGender;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "composition")
    private Set<Branch> branches;


    @Enumerated(EnumType.STRING)
    @Column(name = "composition_status")
    private CompositionStatus compositionStatus;

    public Composition() {
    }
    public Composition(Long id, String title, String description, Integer compositionOrder, Integer time, String illustration, Integer numberOfVersions, Integer numberOfBranches, Date creationDate, Date lastupdateDate, Date suppressionDate, Date definitivSupressionDate, String subGender, String projectMusicalGender, Project project, Set<Branch> branches, CompositionStatus compositionStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.compositionOrder = compositionOrder;
        this.illustration = illustration;
        this.numberOfVersions = numberOfVersions;
        this.numberOfBranches = numberOfBranches;
        this.creationDate = creationDate;
        this.lastupdateDate = lastupdateDate;
        this.suppressionDate = suppressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.subGender = subGender;
        this.projectMusicalGender = projectMusicalGender;
        this.project = project;
        this.branches = branches;
        this.compositionStatus = compositionStatus;
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

    public Integer getNumberOfVersions() {
        return numberOfVersions;
    }
    public void setNumberOfVersions(Integer numberOfVersions) {
        this.numberOfVersions = numberOfVersions;
    }

    public Integer getNumberOfBranches() {
        return numberOfBranches;
    }
    public void setNumberOfBranches(Integer numberOfBranches) {
        this.numberOfBranches = numberOfBranches;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastupdateDate() {
        return lastupdateDate;
    }
    public void setLastupdateDate(Date lastupdateDate) {
        this.lastupdateDate = lastupdateDate;
    }

    public Date getSuppressionDate() {
        return suppressionDate;
    }
    public void setSuppressionDate(Date suppressionDate) {
        this.suppressionDate = suppressionDate;
    }

    public Date getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Date definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public String getSubGender() {
        return subGender;
    }
    public void setSubGender(String subGender) {
        this.subGender = subGender;
    }

    public String getProjectMusicalGender() {
        return projectMusicalGender;
    }
    public void setProjectMusicalGender(String projectMusicalGender) {
        this.projectMusicalGender = projectMusicalGender;
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

}
