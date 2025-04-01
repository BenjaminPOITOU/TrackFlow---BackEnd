package com.eql.cda.track.flow.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
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

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "versions")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long versionNumber;
    private String description;
    private String audioFileUrl;
    @Column(columnDefinition = "TEXT")
    private String metaData;
    private Date creationDate;
    private Date supressionDate;
    private Date definitivSupressionDate;
    private Long parentVersionId;
    private Long mergedSourceId;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "version")
    private List<PlaylistVersion> playlistVersions;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    private List<Annotation> annotations;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL)
    private List<InstrumentChosenByUser> instrumentChosenByUserList;

    @OneToMany(mappedBy = "version", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Access> accesses;




    @ElementCollection(targetClass = VersionInstrumentPreDefined.class, fetch = FetchType.EAGER)
    @CollectionTable(name="version_instrument_predefined", joinColumns =  @JoinColumn(name = "version_id"))
    @Enumerated(EnumType.STRING)
    private List<VersionInstrumentPreDefined> versionInstrumentPreDefinedList;



    public Version() {
    }

    public Version(Long id, String title, Long versionNumber, String description, String audioFileUrl, String metaData, Date creationDate, Date supressionDate, Date definitivSupressionDate, Long parentVersionId, Long mergedSourceId, Branch branch, List<Annotation> annotations, List<InstrumentChosenByUser> instrumentChosenByUserList, Set<Access> accesses, List<VersionInstrumentPreDefined> versionInstrumentPreDefinedList) {
        this.id = id;
        this.title = title;
        this.versionNumber = versionNumber;
        this.description = description;
        this.audioFileUrl = audioFileUrl;
        this.metaData = metaData;
        this.creationDate = creationDate;
        this.supressionDate = supressionDate;
        this.definitivSupressionDate = definitivSupressionDate;
        this.parentVersionId = parentVersionId;
        this.mergedSourceId = mergedSourceId;
        this.branch = branch;
        this.annotations = annotations;
        this.instrumentChosenByUserList = instrumentChosenByUserList;
        this.accesses = accesses;
        this.versionInstrumentPreDefinedList = versionInstrumentPreDefinedList;
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

    public Long getVersionNumber() {
        return versionNumber;
    }
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioFileUrl() {
        return audioFileUrl;
    }
    public void setAudioFileUrl(String audioFileUrl) {
        this.audioFileUrl = audioFileUrl;
    }

    public String getMetaData() {
        return metaData;
    }
    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSupressionDate() {
        return supressionDate;
    }
    public void setSupressionDate(Date supressionDate) {
        this.supressionDate = supressionDate;
    }

    public Date getDefinitivSupressionDate() {
        return definitivSupressionDate;
    }
    public void setDefinitivSupressionDate(Date definitivSupressionDate) {
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

    public List<InstrumentChosenByUser> getInstrumentChosenByUserList() {
        return instrumentChosenByUserList;
    }
    public void setInstrumentChosenByUserList(List<InstrumentChosenByUser> instrumentChosenByUserList) {
        this.instrumentChosenByUserList = instrumentChosenByUserList;
    }

    public List<VersionInstrumentPreDefined> getInstrumentPreDefinedList() {
        return versionInstrumentPreDefinedList;
    }

    public Set<Access> getAccesses() {
        return accesses;
    }
    public void setAccesses(Set<Access> accesses) {
        this.accesses = accesses;
    }
}
