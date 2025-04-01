package com.eql.cda.track.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="project_musical_genders_added")
public class ProjectMusicalGenderAdded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userMusicalGenderAdded;
    private String color;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public ProjectMusicalGenderAdded() {
    }
    public ProjectMusicalGenderAdded(Long id, String userMusicalGenderAdded, String color, Project project) {
        this.id = id;
        this.userMusicalGenderAdded = userMusicalGenderAdded;
        this.color = color;
        this.project = project;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserMusicalGenderAdded() {
        return userMusicalGenderAdded;
    }
    public void setUserMusicalGenderAdded(String userMusicalGenderAdded) {
        this.userMusicalGenderAdded = userMusicalGenderAdded;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }
}
