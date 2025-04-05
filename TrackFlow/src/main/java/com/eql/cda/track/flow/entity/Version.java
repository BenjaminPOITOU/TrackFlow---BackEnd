package com.eql.cda.track.flow.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "versions")
@EntityListeners(AuditingEntityListener.class)
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String name;
    private String author;
    private String bpm;

    @Column(name = "`key`")
    private String key;
    private Integer durationSeconds;
    private String audioFileUrl;

    @ElementCollection(fetch = FetchType.LAZY) // LAZY est souvent préférable pour les collections
    @CollectionTable(name = "version_metadata", // Nom de la table séparée pour stocker la Map
            joinColumns = @JoinColumn(name = "version_id")) // Clé étrangère vers la table 'versions'
    @MapKeyColumn(name = "metadata_key") // Nom de la colonne pour stocker la clé de la Map
    @Column(name = "metadata_value")
    private Map<String, String> metadata;

    @Column(nullable = true)
    @CreatedDate
    private LocalDateTime createdDate;
    private LocalDateTime supressionDate;
    private LocalDateTime definitivSupressionDate;
    private Long parentVersionId;
    private Long mergedSourceId;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "version")
    private List<PlaylistVersion> playlistVersions;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    private List<Annotation> annotations;


    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Access> accesses;



    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VersionInstrument> instruments = new HashSet<>(); // Initialiser la collection


                    /// METHODES ////
    public void addInstrumentPreDefined(VersionInstrumentPreDefined instrumentEnum) {
        VersionInstrument versionInstrument = new VersionInstrument(this, instrumentEnum);
        this.instruments.add(versionInstrument);
    }

    public void removeInstrumentFromVersion(VersionInstrument versionInstrument) {
        this.instruments.remove(versionInstrument);
        versionInstrument.setVersion(null); // Rompre le lien
    }

                ///FIN METHODES ////

    public Version() {
    }

    public Version(Long id, String name, String author, String bpm, String key, Integer durationSeconds,String audioFileUrl, Map<String, String> metadata, LocalDateTime createdDate, LocalDateTime supressionDate, LocalDateTime definitivSupressionDate, Long parentVersionId, Long mergedSourceId, Branch branch, List<PlaylistVersion> playlistVersions, List<Annotation> annotations, Set<Access> accesses) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.bpm = bpm;
        this.key = key;
        this.durationSeconds = durationSeconds;
        this.audioFileUrl = audioFileUrl;
        this.metadata = metadata;
        this.createdDate = createdDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.parentVersionId = parentVersionId;
        this.mergedSourceId = mergedSourceId;
        this.branch = branch;
        this.playlistVersions = playlistVersions;
        this.annotations = annotations;
        this.accesses = accesses;
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

    public String getAudioFileUrl() {
        return audioFileUrl;
    }
    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public Long getParentVersionId() {
        return parentVersionId;
    }
    public void setParentVersionId(Long parentVersionId) {
        this.parentVersionId = parentVersionId;
    }

    public Long getMergedSourceId() {
        return mergedSourceId;
    }
    public void setMergedSourceId(Long mergedSourceId) {
        this.mergedSourceId = mergedSourceId;
    }

    public Branch getBranch() {
        return branch;
    }
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
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

    public List<PlaylistVersion> getPlaylistVersions() {
        return playlistVersions;
    }
    public void setPlaylistVersions(List<PlaylistVersion> playlistVersions) {
        this.playlistVersions = playlistVersions;
    }

    public Set<Access> getAccesses() {
        return accesses;
    }
    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public Set<VersionInstrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<VersionInstrument> instruments) {
        this.instruments = instruments;
    }
}
