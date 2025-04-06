package com.eql.cda.track.flow.entity;

import com.eql.cda.track.flow.validation.Constants;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = Constants.PROJECT_TITLE_MAX_LENGTH, message = Constants.PROJECT_TITLE_MAX_LENGTH_MSG)
    @Column(length = Constants.PROJECT_TITLE_MAX_LENGTH)
    private String title;

    @Size(max = Constants.PROJECT_DESC_MAX_LENGTH, message = Constants.PROJECT_DESC_MAX_LENGTH_MSG)
    @Column(length = Constants.PROJECT_DESC_MAX_LENGTH)
    private String description;

    private String illustration;
    private boolean isArchived;
    private Integer projectOrder;

    @CreatedDate
    @Column(nullable = false, updatable = false) // Bonnes pratiques audit
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false) // Bonnes pratiques audit
    private LocalDateTime lastUpdateDate;

    private LocalDateTime supressionDate;
    private LocalDateTime definitivSupressionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ElementCollection(targetClass = ProjectMusicalGenderPreDefined.class, fetch = FetchType.LAZY)
    @CollectionTable(name="project_musical_gender_predefined", joinColumns = @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    @Column(name="gender")
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined = new HashSet<>(); // <-- ****** TYPE CHANGÉ EN Set ******

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Composition> compositions = new HashSet<>(); // <-- ****** TYPE CHANGÉ EN Set ******


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
    @CollectionTable(name="project_purpose", joinColumns =  @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    @Column(name="purpose")
    // Préférer Set pour les collections non ordonnées et éviter les doublons
    private Set<ProjectPurpose> projectPurposes = new HashSet<>();





    public Project() {
    }

    public Project(Long id, String title, String description, String illustration, boolean isArchived, Integer projectOrder, LocalDateTime createdDate, LocalDateTime lastUpdateDate, LocalDateTime supressionDate, LocalDateTime definitivSupressionDate, User user, Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined, Set<Composition> compositions, ProjectStatus projectStatus, ProjectType projectType, ProjectCommercialStatus projectCommercialStatus, Set<ProjectPurpose> projectPurposes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.illustration = illustration;
        this.isArchived = isArchived;
        this.projectOrder = projectOrder;
        this.createdDate = createdDate;
        this.lastUpdateDate = lastUpdateDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.user = user;
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
        this.compositions = compositions;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectCommercialStatus = projectCommercialStatus;
        this.projectPurposes = projectPurposes;
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

    public String getIllustration() {
        return illustration;
    }
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public Boolean getArchived() {
        return isArchived;
    }
    public void setArchived(Boolean archived) {
        isArchived = archived;
    }


    public Integer getProjectOrder() {
        return projectOrder;
    }
    public void setProjectOrder(Integer projectOrder) {
        this.projectOrder = projectOrder;
    }


    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(LocalDateTime supressionDate) {
        this.supressionDate = supressionDate;
    }

    public LocalDateTime getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }

    public void setDefinitivSupressionDate(LocalDateTime definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public boolean isArchived() {
        return isArchived;
    }
    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) {
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
    }

    public Set<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(Set<Composition> compositions) {
        this.compositions = compositions;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }
    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public ProjectType getProjectType() {
        return projectType;
    }
    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public ProjectCommercialStatus getProjectCommercialStatus() {
        return projectCommercialStatus;
    }
    public void setProjectCommercialStatus(ProjectCommercialStatus projectCommercialStatus) {
        this.projectCommercialStatus = projectCommercialStatus;
    }


    public Set<ProjectPurpose> getProjectPurposes() {
        return projectPurposes;
    }

    public void setProjectPurposes(Set<ProjectPurpose> projectPurposes) {
        this.projectPurposes = projectPurposes;
    }

    public Set<ProjectMusicalGenderPreDefined> getProjectMusicalGendersPreDefined() {
        return projectMusicalGendersPreDefined;
    }
}
