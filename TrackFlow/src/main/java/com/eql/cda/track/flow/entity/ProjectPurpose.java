package com.eql.cda.track.flow.entity;

public enum ProjectPurpose {

    LABEL("Pour un label"),
    STREAMING("Plateforme de streaming"),
    AGENCE("Agence"),
    CINEMA("Cinéma"),
    VIDEOGAME("Jeu vidéo"),
    MEDIA("Médias"),
    MUSEE("Musée"),
    SPECTACLE("Spectacle"),
    CORPORATIF("Usage corporatif"),
    PERSONNEL("Usage personnel"),
    COMMANDE("Commande"),
    APPLICATION("Application"),
    PLATEFORME("Plateforme"),
    PEDAGOGIQUE("Pédagogique"),
    RADIO("Radio");

    private final String label;

    private ProjectPurpose(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
