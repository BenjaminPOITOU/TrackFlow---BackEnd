package com.eql.cda.track.flow.entity;

public enum CompositionStatus {

    EBAUCHE("Ébauche"),
    EN_COURS("En cours"),
    A_FINALISER("À finaliser"),
    TERMINE("Terminé");

    private final String label;

    private CompositionStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
