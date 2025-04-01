package com.eql.cda.track.flow.entity;

public enum AnnotationRight {

    LISTEN_ONLY("Ecoute seule"),
    LISTEN_AND_ANNOTATE("Ecoute et Annotation");

    private final String Label;

    AnnotationRight(String label) {
        Label = label;
    }

    public String getLabel() {
        return Label;
    }
}
