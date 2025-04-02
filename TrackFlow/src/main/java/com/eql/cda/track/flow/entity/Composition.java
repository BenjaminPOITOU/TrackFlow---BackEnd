package com.eql.cda.track.flow.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastUpdateDate;

    private LocalDateTime suppressionDate;
    private LocalDateTime definitivSupressionDate;


    @ElementCollection(fetch = FetchType.LAZY) // LAZY est souvent préférable pour les collections
    @CollectionTable(name = "composition_sub_genders", joinColumns = @JoinColumn(name = "composition_id")) // Table de jointure pour les sous-genres
    @Column(name = "sub_gender") // Nom de la colonne dans la table de jointure
    private List<String> subGenders = new ArrayList<>(); // Initialiser la liste

    // --- Champs supprimés ---
    // private List<ProjectMusicalGenderPreDefined> projectMusicalGender; // Supprimé
    // private List<ProjectMusicalGenderAdded> projectMusicalGenderAdded; // Supprimé (cause principale de l'erreur)


    @ManyToOne(fetch = FetchType.LAZY) // LAZY est souvent préférable pour les relations ToOne
    @JoinColumn(name = "project_id", nullable = false) // Une composition doit appartenir à un projet
    @JsonBackReference
    private Project project;

    // Assurez-vous que l'entité 'Branch' existe, qu'elle est annotée @Entity
    // et qu'elle a un champ 'private Composition composition;' annoté @ManyToOne
    @OneToMany(mappedBy = "composition", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY) // cascade et orphanRemoval si besoin
    private Set<Branch> branches = new HashSet<>(); // Initialiser le set

    @Enumerated(EnumType.STRING)
    @Column(name = "composition_status")
    private CompositionStatus compositionStatus;

    // Constructeur par défaut requis par JPA
    public Composition() {
    }

    // --- Constructeur mis à jour (sans les champs supprimés) ---
    public Composition(Long id, String title, String description, Integer compositionOrder, String illustration, LocalDateTime createdDate, LocalDateTime lastUpdateDate, LocalDateTime suppressionDate, LocalDateTime definitivSupressionDate, List<String> subGenders, /* Supprimé */ /* Supprimé */ Project project, Set<Branch> branches, CompositionStatus compositionStatus) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.compositionOrder = compositionOrder;
        this.illustration = illustration;
        this.createdDate = createdDate;
        this.lastUpdateDate = lastUpdateDate;
        this.suppressionDate = suppressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.subGenders = subGenders != null ? new ArrayList<>(subGenders) : new ArrayList<>(); // Copie défensive
        this.project = project;
        this.branches = branches != null ? new HashSet<>(branches) : new HashSet<>(); // Copie défensive
        this.compositionStatus = compositionStatus;
    }


    // --- Getters and Setters (mis à jour, sans ceux des champs supprimés) ---

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public LocalDateTime getSuppressionDate() {
        return suppressionDate;
    }
    public void setSuppressionDate(LocalDateTime suppressionDate) {
        this.suppressionDate = suppressionDate;
    }

    public LocalDateTime getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(LocalDateTime definitivSupressionDate) {
        this.definitivSupressionDate = definitivSupressionDate;
    }

    public List<String> getSubGenders() {
        // Retourner une copie pour éviter les modifications externes non désirées si nécessaire
        return subGenders; // Ou return new ArrayList<>(subGenders);
    }
    public void setSubGenders(List<String> subGenders) {
        // Copie défensive lors du set
        this.subGenders = subGenders != null ? new ArrayList<>(subGenders) : new ArrayList<>();
    }

    // --- Getters/Setters pour projectMusicalGender et projectMusicalGenderAdded sont SUPPRIMÉS ---

    public Project getProject() {
        return project;
    }
    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Branch> getBranches() {
        // Retourner une copie pour éviter les modifications externes non désirées si nécessaire
        return branches; // Ou return new HashSet<>(branches);
    }
    public void setBranches(Set<Branch> branches) {
        // Copie défensive lors du set
        this.branches = branches != null ? new HashSet<>(branches) : new HashSet<>();
    }

    public CompositionStatus getCompositionStatus() {
        return compositionStatus;
    }
    public void setCompositionStatus(CompositionStatus compositionStatus) {
        this.compositionStatus = compositionStatus;
    }
}

