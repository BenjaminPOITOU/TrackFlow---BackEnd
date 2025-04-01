package com.eql.cda.track.flow.entity;

public enum ProjectCommercialStatus {


    LIBRE("Libre de droits"),
    EXCLUSIF("Exclusif"),
    COMMONS("Creative Commons"),
    TOUTDROIT("Tous droits réservés"),
    ENATTENTE("En attente"),
    DISPOLICENCE("Disponible sous licence"),
    DEMONSTRATION("Démonstration"),
    NEGOCIATION("En négociation"),
    INTERNE("Usage interne"),
    PROMOTION("Promotionnel"),
    EXPLOITATION("En exploitation"),
    NONCOMMERCIAL("Non commercial"),
    LIMITEE("Licence limitée"),
    UNIQUE("Édition unique"),
    PARTAGE("Partage autorisé");

    private final String label;

    private ProjectCommercialStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
