package com.eql.cda.track.flow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a musical project in the system.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 150)
    @Column(length = 150)
    private String title;

    @Size(max = 2000)
    @Column(length = 2000)
    private String description;

    private String illustration;
    private boolean archived;
    private Integer projectOrder;

    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @Column(nullable = false)
    private Instant lastUpdateDate;

    private Instant supressionDate;
    private Instant definitivSupressionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ElementCollection(targetClass = ProjectMusicalGenderPreDefined.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "project_musical_gender_predefined", joinColumns = @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Composition> compositions = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_commercial_status")
    private ProjectCommercialStatus projectCommercialStatus;

    @ElementCollection(targetClass = ProjectPurpose.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "project_purpose", joinColumns = @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose")
    private Set<ProjectPurpose> projectPurposes = new HashSet<>();

    public Project() {
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIllustration() { return illustration; }
    public void setIllustration(String illustration) { this.illustration = illustration; }
    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public Integer getProjectOrder() { return projectOrder; }
    public void setProjectOrder(Integer projectOrder) { this.projectOrder = projectOrder; }
    public Instant getCreatedDate() { return createdDate; }
    public void setCreatedDate(Instant createdDate) { this.createdDate = createdDate; }
    public Instant getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(Instant lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }
    public Instant getSupressionDate() { return supressionDate; }
    public void setSupressionDate(Instant supressionDate) { this.supressionDate = supressionDate; }
    public Instant getDefinitivSupressionDate() { return definitivSupressionDate; }
    public void setDefinitivSupressionDate(Instant definitivSupressionDate) { this.definitivSupressionDate = definitivSupressionDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGendersPreDefined() { return projectMusicalGendersPreDefined; }
    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) { this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined; }
    public Set<Composition> getCompositions() { return compositions; }
    public void setCompositions(Set<Composition> compositions) { this.compositions = compositions; }
    public ProjectStatus getProjectStatus() { return projectStatus; }
    public void setProjectStatus(ProjectStatus projectStatus) { this.projectStatus = projectStatus; }
    public ProjectType getProjectType() { return projectType; }
    public void setProjectType(ProjectType projectType) { this.projectType = projectType; }
    public ProjectCommercialStatus getProjectCommercialStatus() { return projectCommercialStatus; }
    public void setProjectCommercialStatus(ProjectCommercialStatus projectCommercialStatus) { this.projectCommercialStatus = projectCommercialStatus; }
    public Set<ProjectPurpose> getProjectPurposes() { return projectPurposes; }
    public void setProjectPurposes(Set<ProjectPurpose> projectPurposes) { this.projectPurposes = projectPurposes; }
}