package com.eql.cda.track.flow.entity;

public enum ProjectType {


    ALBUM("Album"),
    EP("EP"),
    SINGLE("Single"),
    REMIX("Remix"),
    BO("Bande originale"),
    PODCAST("Podcast"),
    PUBLICITE("Publicité"),
    VIDEOGAME("Jeu vidéo"),
    INSTALAUDIO("Installation audio"),
    LIVEPERFORMANCE("Performance live"),
    MAQUETTE("Maquette"),
    COLLABORATION("Collaboration"),
    EDUCATIF("Éducatif"),
    IMPROVISATION("Improvisation"),
    EXPERIMENTAL("Expérimental");

    private final String label;

    private ProjectType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
