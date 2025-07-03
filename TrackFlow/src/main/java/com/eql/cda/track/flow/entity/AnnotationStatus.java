package com.eql.cda.track.flow.entity;

/**
 * Defines the lifecycle status of an annotation.
 */
public enum AnnotationStatus {

    EN_REFLEXION("En réflexion"),
    A_TRAITER("À traiter"),
    RESOLUE("Résolue");

    private final String label;

    AnnotationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}