package com.eql.cda.track.flow.entity;

/**
 * Defines the access rights related to annotations.
 * This is currently not directly used on the Annotation entity itself.
 */
public enum AnnotationRight {

    LISTEN_ONLY("Ecoute seule"),
    LISTEN_AND_ANNOTATE("Ecoute et Annotation");

    private final String label;

    AnnotationRight(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}