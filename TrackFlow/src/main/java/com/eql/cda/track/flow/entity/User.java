package com.eql.cda.track.flow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * Represents a base user in the system.
 * This abstract class provides common properties and contains both the fundamental
 * security role and the optional UI profile type.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;

    @JsonIgnore
    @NotNull
    @Column(unique = true)
    private String login;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_role", nullable = false)
    private SecurityRole securityRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "profile_type") // Can be null for ADMIN
    private ProfileType profileType;

    @CreatedDate
    @Column(updatable = false)
    private Instant creationDate;

    @LastModifiedDate
    private Instant updateDate;

    private Instant suppressionDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference("user-project")
    private List<Project> projects;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private Set<Access> accesses;

    public User() {}

    // Getters and Setters for all fields...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public SecurityRole getSecurityRole() { return securityRole; }
    public void setSecurityRole(SecurityRole securityRole) { this.securityRole = securityRole; }
    public ProfileType getProfileType() { return profileType; }
    public void setProfileType(ProfileType profileType) { this.profileType = profileType; }
    public Instant getCreationDate() { return creationDate; }
    public void setCreationDate(Instant creationDate) { this.creationDate = creationDate; }
    public Instant getUpdateDate() { return updateDate; }
    public void setUpdateDate(Instant updateDate) { this.updateDate = updateDate; }
    public Instant getSuppressionDate() { return suppressionDate; }
    public void setSuppressionDate(Instant suppressionDate) { this.suppressionDate = suppressionDate; }
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    public Set<Access> getAccesses() { return accesses; }
    public void setAccesses(Set<Access> accesses) { this.accesses = accesses; }
}