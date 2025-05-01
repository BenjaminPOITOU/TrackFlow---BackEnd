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


    /**
     * Convertit un libellé en sa constante d'énumération correspondante.
     * @param label Le libellé à convertir (ex: "Mixage")
     * @return La constante d'énumération correspondante
     * @throws IllegalArgumentException si aucune correspondance n'est trouvée
     */
    public static AnnotationCategory fromLabel(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }

        for (AnnotationCategory category : values()) {
            if (category.getLabel().equals(label)) {
                return category;
            }
        }

        throw new IllegalArgumentException("No AnnotationCategory found for label: " + label);
    }

    public String getLabel() {
        return label;
    }
}
