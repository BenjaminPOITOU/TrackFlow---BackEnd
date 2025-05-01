package com.eql.cda.track.flow.entity;

public enum AnnotationStatus {


    EN_REFLEXION("En réflexion"),
    A_TRAITER("À traiter"),
    RESOLUE("Résolue");

    private final String label;

    private AnnotationStatus(String label) {
        this.label = label;
    }


    /**
     * Convertit un libellé en sa constante d'énumération correspondante.
     * @param label Le libellé à convertir (ex: "En réflexion")
     * @return La constante d'énumération correspondante
     * @throws IllegalArgumentException si aucune correspondance n'est trouvée
     */
    public static AnnotationStatus fromLabel(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }

        for (AnnotationStatus status : values()) {
            if (status.getLabel().equals(label)) {
                return status;
            }
        }

        throw new IllegalArgumentException("No AnnotationStatus found for label: " + label);
    }

    public String getLabel() {
        return label;
    }
}
