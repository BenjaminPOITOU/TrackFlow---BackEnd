package com.eql.cda.track.flow.entity;

import com.eql.cda.track.flow.validation.Constants;
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
    private LocalDateTime createdDate;


    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    private LocalDateTime supressionDate;
    private LocalDateTime definitivSupressionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectTag> projectTags;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Composition> compositions=new HashSet<>();


    @Enumerated(EnumType.STRING)
    @Column(name = "project_status")
    private ProjectStatus projectStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type")
    private ProjectType projectType;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_commercial_status")
    private ProjectCommercialStatus projectCommercialStatus;

    @ElementCollection(targetClass = ProjectPurpose.class, fetch = FetchType.EAGER)
    @CollectionTable(name="project_purpose", joinColumns =  @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    private Set<ProjectPurpose> projectPurposes;

    @ElementCollection(targetClass = ProjectMusicalGenderPreDefined.class, fetch = FetchType.EAGER)
    @CollectionTable(name="project_musical_gender_predefined", joinColumns =  @JoinColumn(name = "project_id"))
    @Enumerated(EnumType.STRING)
    private Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined;






    public Project() {
    }

    public Project(Long id, String title, String description, String illustration, Boolean isArchived, Integer projectOrder, LocalDateTime creationDate, LocalDateTime lastUpdateDate, LocalDateTime supressionDate, LocalDateTime definitivSupressionDate, User user, Set<ProjectTag> projectTags, Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet, Set<Composition> compositions, ProjectStatus projectStatus, ProjectType projectType, ProjectCommercialStatus projectCommercialStatus, Set<ProjectPurpose> projectPurposes, Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.illustration = illustration;
        this.isArchived = isArchived;
        this.projectOrder = projectOrder;
        this.createdDate = creationDate;
        this.lastUpdateDate = lastUpdateDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.user = user;
        this.projectTags = projectTags;
        this.projectMusicalGenderAddedSet = projectMusicalGenderAddedSet;
        this.compositions = compositions;
        this.projectStatus = projectStatus;
        this.projectType = projectType;
        this.projectCommercialStatus = projectCommercialStatus;
        this.projectPurposes = projectPurposes;
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
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

    public LocalDateTime getCreationDate() {
        return createdDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.createdDate = creationDate;
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

    public Set<ProjectTag> getProjectTags() {
        return projectTags;
    }
    public void setProjectTags(Set<ProjectTag> projectTags) {
        this.projectTags = projectTags;
    }

    public Set<ProjectMusicalGenderAdded> getProjectMusicalGenderSet() {
        return projectMusicalGenderAddedSet;
    }
    public void setProjectMusicalGenders(Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet) {
        this.projectMusicalGenderAddedSet = projectMusicalGenderAddedSet;
    }

    public Set<Composition> getCompositons() {
        return compositions;
    }
    public void setCompositons(Set<Composition> compositions) {
        this.compositions = compositions;
    }

    public Set<ProjectMusicalGenderAdded> getProjectMusicalGenderAddedSet() {
        return projectMusicalGenderAddedSet;
    }
    public void setProjectMusicalGenderAddedSet(Set<ProjectMusicalGenderAdded> projectMusicalGenderAddedSet) {
        this.projectMusicalGenderAddedSet = projectMusicalGenderAddedSet;
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
    public void setProjectMusicalGendersPreDefined(Set<ProjectMusicalGenderPreDefined> projectMusicalGendersPreDefined) {
        this.projectMusicalGendersPreDefined = projectMusicalGendersPreDefined;
    }
}
