package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.dto.annotationDto.AnnotationCreateDto;
import com.eql.cda.track.flow.dto.annotationDto.NewVersionAnnotationDto;
import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class VersionCreateDto {


    @Positive(message = "Branch ID must be a positive number if provided.")
    private Long branchId; // Soit ça...

    /** Nom de la nouvelle branche à créer. Null si on utilise une branche existante (branchId doit être fourni). */
    @Size(max = 100, message = "New branch name cannot exceed 100 characters.")
    private String newBranchName; // ... soit ça (et les deux suivants)

    /** Description pour la nouvelle branche (optionnelle mais recommandée si newBranchName est fourni). */
    @Size(max = 500, message = "Branch description cannot exceed 500 characters.")
    private String newBranchDescription; // Renommé depuis "branchDescription" pour la clarté

    /** ID de la branche parente. Requis si une nouvelle branche (non 'main') est créée (newBranchName fourni). */
    @Positive(message = "Parent Branch ID must be a positive number if creating a new branch.")
    private Long parentBranchId; // Nécessaire pour savoir où attacher la nouvelle branche dans l'historique

    // --- Info Upload GCS ---
    /** Nom unique du fichier audio stocké sur Google Cloud Storage. */
    @NotBlank(message = "Unique file name from storage is mandatory.")
    @Size(max = 1024, message = "Unique file name is too long (max 1024 chars).") // Ajuste la taille si nécessaire
    private String uniqueFileName; // Le service l'utilisera pour construire l'URL complète.

    // --- Info Version Parente ---


    @Positive(message = "Parent Version ID must be positive if provided.")
    private Long parentVersionId;

    // --- Métadonnées Audio ---
    /** Durée du fichier audio en secondes entières. */
    @PositiveOrZero(message = "Duration must be zero or positive.")
    private Integer durationSeconds; // Peut être null si l'extraction échoue? Ajoute @NotNull si obligatoire.

    /** Tempo du morceau (Beats Per Minute). */
    // Permettre null si non extrait ? Ou valeur par défaut ? @NotNull ?
    // Valider le format ? Ex: @Pattern(regexp="\\d{1,3}(\\.\\d{1,2})?") si tu attends un nombre.
    @Size(max = 10, message = "BPM string representation cannot exceed 10 characters.")
    private String bpm; // Ou utiliser Double/BigDecimal avec validation de format/range.

    /** Tonalité du morceau. */
    // Permettre null ? @NotNull ?
    @Size(max = 10, message = "Musical key cannot exceed 10 characters.")
    private String key; // Nom "key" géré par @Column sur l'entité.

    // --- Instruments pour la NOUVELLE version ---
    /** Liste des instruments prédéfinis à associer à cette nouvelle version. */
    // Si tu veux la description -> @Valid private List<InstrumentSelectionDto> instruments;
    @NotNull(message = "Instrument list cannot be null (can be empty).")
    private Set<VersionInstrumentPreDefined> versionInstrumentPreDefinedList;

    // --- Actions sur les annotations de la version N-1 (parentVersionId) ---

    /** IDs des annotations de la version parent (parentVersionId) qui doivent être marquées comme RESOLUE. */
    @NotNull(message = "List of annotation IDs to resolve cannot be null (can be empty).")
    private List<Long> annotationIdsToResolve;

    /** IDs des annotations de la version parent (parentVersionId) qui doivent être (soft) supprimées. */
    @NotNull(message = "List of annotation IDs to delete cannot be null (can be empty).")
    private List<Long> annotationIdsToDelete;

    // --- Annotations à créer pour la NOUVELLE version (N) ---

    /**
     * Liste des annotations à créer pour cette nouvelle version.
     * Peut inclure des annotations reportées/modifiées de la version N-1 (via sourceAnnotationId dans NewVersionAnnotationDto)
     * ou des annotations entièrement nouvelles.
     */
    @Valid // Active la validation sur chaque NewVersionAnnotationDto dans la liste.
    @NotNull(message = "List of annotations to create cannot be null (can be empty).")
    private List<NewVersionAnnotationDto> annotationsToCreate;

    // --- Map Métadonnées (Rôle à confirmer) ---
    /** Métadonnées supplémentaires (clé-valeur). Leur utilité précise doit être définie. */
    private Map<String, String> metadata;


    public VersionCreateDto() {
    }

    public VersionCreateDto(Long branchId, String newBranchName, String newBranchDescription, Long parentBranchId, String uniqueFileName, Long parentVersionId, Integer durationSeconds, String bpm, String key, Set<VersionInstrumentPreDefined> versionInstrumentPreDefinedList, List<Long> annotationIdsToResolve, List<Long> annotationIdsToDelete, List<NewVersionAnnotationDto> annotationsToCreate, Map<String, String> metadata) {
        this.branchId = branchId;
        this.newBranchName = newBranchName;
        this.newBranchDescription = newBranchDescription;
        this.parentBranchId = parentBranchId;
        this.uniqueFileName = uniqueFileName;
        this.parentVersionId = parentVersionId;
        this.durationSeconds = durationSeconds;
        this.bpm = bpm;
        this.key = key;
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
        this.annotationIdsToResolve = annotationIdsToResolve;
        this.annotationIdsToDelete = annotationIdsToDelete;
        this.annotationsToCreate = annotationsToCreate;
        this.metadata = metadata;
    }

    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getParentBranchId() {
        return parentBranchId;
    }

    public void setParentBranchId(Long parentBranchId) {
        this.parentBranchId = parentBranchId;
    }

    public String getNewBranchName() {
        return newBranchName;
    }
    public void setNewBranchName(String newBranchName) {
        this.newBranchName = newBranchName;
    }

    public String getNewBranchDescription() {
        return newBranchDescription;
    }
    public void setNewBranchDescription(String newBranchDescription) {
        this.newBranchDescription = newBranchDescription;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }
    public void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }

    public List<Long> getAnnotationIdsToResolve() {
        return annotationIdsToResolve;
    }
    public void setAnnotationIdsToResolve(List<Long> annotationIdsToResolve) {
        this.annotationIdsToResolve = annotationIdsToResolve;
    }

    public List<Long> getAnnotationIdsToDelete() {
        return annotationIdsToDelete;
    }
    public void setAnnotationIdsToDelete(List<Long> annotationIdsToDelete) {
        this.annotationIdsToDelete = annotationIdsToDelete;
    }

    public List<NewVersionAnnotationDto> getAnnotationsToCreate() {
        return annotationsToCreate;
    }
    public void setAnnotationsToCreate(List<NewVersionAnnotationDto> annotationsToCreate) {
        this.annotationsToCreate = annotationsToCreate;
    }

    public Long getParentVersionId() {
        return parentVersionId;
    }
    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Set<VersionInstrumentPreDefined> getVersionInstrumentPreDefinedList() {
        return versionInstrumentPreDefinedList;
    }

    public void setVersionInstrumentPreDefinedList(Set<VersionInstrumentPreDefined> versionInstrumentPreDefinedList) {
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
    }

    public String getBpm() {
        return bpm;
    }
    public void setBpm(String bpm) {
        this.bpm = bpm;
    }

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

}
