package com.eql.cda.track.flow.entity;

/**
 * Defines the functional categories for an annotation.
 */
public enum AnnotationCategory {

    MIXAGE("Mixage"),
    ARRANGEMENT("Arrangement"),
    MELODIC_IDEA("Idée mélodique"),
    STRUCTURE("Structure"),
    LYRICS("Paroles"),
    TECHNICAL("Technique"),
    GENERAL("Général");

    private final String label;

    AnnotationCategory(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}