package com.eql.cda.track.flow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents the join entity for the many-to-many relationship
 * between a {@link Version} and a {@link VersionInstrumentPreDefined} enum.
 * Each instance of this class links one version to one specific instrument.
 */
@Entity
@Table(name = "version_instruments")
public class VersionInstrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @Enumerated(EnumType.STRING)
    @Column(name = "instrument", nullable = false)
    private VersionInstrumentPreDefined instrument;

    /**
     * Default constructor required by the persistence framework (JPA).
     */
    public VersionInstrument() {
    }

    /**
     * Constructs a new VersionInstrument association.
     *
     * @param version The version this instrument is being associated with.
     * @param instrument The predefined instrument enum to associate.
     */
    public VersionInstrument(Version version, VersionInstrumentPreDefined instrument) {
        this.version = version;
        this.instrument = instrument;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Version getVersion() {
        return version;
    }
    public void setVersion(Version version) {
        this.version = version;
    }

    public VersionInstrumentPreDefined getInstrument() {
        return instrument;
    }
    public void setInstrument(VersionInstrumentPreDefined instrument) {
        this.instrument = instrument;
    }
}