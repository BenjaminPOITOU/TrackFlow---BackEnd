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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "version_instruments")
public class VersionInstrument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Important: LAZY pour éviter chargement inutile
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @Enumerated(EnumType.STRING) // Stocke le nom de l'enum ("GUITARE") plutôt que l'ordinal (0)
    @Column(name = "instrument", nullable = false)
    private VersionInstrumentPreDefined instrument;

    // --- Champs Optionnels Futurs ---
    // private Integer volume;
    // private Integer pan;
    // ... autres réglages spécifiques à l'instrument pour cette version

    public VersionInstrument() {
    }

    public VersionInstrument(Version version, VersionInstrumentPreDefined instrument) {
        this.version = version;
        this.instrument = instrument;
    }

    public VersionInstrumentPreDefined getInstrument() {
        return instrument;
    }

    public void setInstrument(VersionInstrumentPreDefined instrument) {
        this.instrument = instrument;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
