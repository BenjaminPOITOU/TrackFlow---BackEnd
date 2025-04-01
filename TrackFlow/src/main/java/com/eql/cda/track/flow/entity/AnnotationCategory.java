package com.eql.cda.track.flow.entity;

import jakarta.persistence.Entity;


public enum AnnotationCategory {

    MIXAGE("Mixage"),
    ARRANGEMENT("Arrangement"),
    MELODIC_IDEA("Idée mélodique"),
    STRUCTURE("Structure"),
    LYRICS("Paroles"),
    TECHNICAL("Technique"),
    GENERAL("Général");

    private final String label;

    private AnnotationCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
