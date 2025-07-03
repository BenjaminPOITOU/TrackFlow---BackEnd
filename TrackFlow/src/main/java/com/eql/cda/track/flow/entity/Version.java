package com.eql.cda.track.flow.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.*;

/**
 * Represents a single, immutable version of a track within a branch.
 * A version captures a specific state of a musical piece, including its audio file,
 * metadata, and associated annotations and instruments.
 */
@Entity
@Table(name = "versions")
@EntityListeners(AuditingEntityListener.class)
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String author;
    private String bpm;

    @Column(name = "`key`")
    private String key;
    private Integer durationSeconds;
    private String audioFileUrl;
    private Long parentVersionId;
    private Long mergedSourceId;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    private Instant supressionDate;
    private Instant definitivSupressionDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "version_metadata", joinColumns = @JoinColumn(name = "version_id"))
    @MapKeyColumn(name = "metadata_key")
    @Column(name = "metadata_value")
    private Map<String, String> metadata = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VersionInstrument> instruments = new HashSet<>();

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Annotation> annotations = new ArrayList<>();

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Access> accesses = new HashSet<>();

    @OneToMany(mappedBy = "version")
    private List<PlaylistVersion> playlistVersions = new ArrayList<>();

    /**
     * Default constructor required by the persistence framework (JPA).
     */
    public Version() {
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

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
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

    public Instant getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(Instant supressionDate) {
        this.supressionDate = supressionDate;
    }

    public Instant getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Instant definitivSupressionDate) {
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

    public List<PlaylistVersion> getPlaylistVersions() {
        return playlistVersions;
    }
    public void setPlaylistVersions(List<PlaylistVersion> playlistVersions) {
        this.playlistVersions = playlistVersions;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }
    public void setAnnotations(List<Annotation> annotations) {
        this.annotations = annotations;
    }

    public Set<Access> getAccesses() {
        return accesses;
    }
    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }

    public Set<VersionInstrument> getInstruments() {
        return instruments;
    }
    public void setInstruments(Set<VersionInstrument> instruments) {
        this.instruments = instruments;
    }
}