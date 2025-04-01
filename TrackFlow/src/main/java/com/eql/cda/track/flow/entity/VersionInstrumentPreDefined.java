package com.eql.cda.track.flow.entity;

public enum VersionInstrumentPreDefined {

    // Cordes
    GUITARE("Guitare"),
    GUITARE_BASSE("Guitare basse"),
    VIOLON("Violon"),
    VIOLONCELLE("Violoncelle"),
    CONTREBASSE("Contrebasse"),
    HARPE("Harpe"),
    BANJO("Banjo"),

    // Vents
    TROMPETTE("Trompette"),
    SAXOPHONE("Saxophone"),
    FLUTE("Flûte traversière"),
    CLARINETTE("Clarinette"),
    HAUTBOIS("Hautbois"),
    TROMBONE("Trombone"),
    TUBA("Tuba"),

    // Claviers
    PIANO("Piano"),
    PIANO_ELECTRIQUE("Piano électrique"),
    CLAVECIN("Clavecin"),
    SYNTHETISEUR("Synthétiseur"),
    ORGUE("Orgue"),

    // Percussions
    BATTERIE("Batterie"),
    CAJON("Cajon"),
    XYLOPHONE("Xylophone"),
    MARIMBA("Marimba"),
    VIBRAPHONE("Vibraphone"),
    TIMBALES("Timbales"),

    // Électronique
    MAO("MAO (Musique Assistée par Ordinateur)"),
    PLATINES("Platines DJ"),
    SAMPLER("Sampler"),
    SEQUENCEUR("Séquenceur"),

    // Autres
    CHANT("Chant"),
    BEATBOX("Beatbox"),
    HARMONICA("Harmonica"),
    ACCORDEON("Accordéon");

    private final String label;

    VersionInstrumentPreDefined(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
