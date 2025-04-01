package com.eql.cda.track.flow.entity;

public enum ProjectMusicalGenderPreDefined {

        ROCK("Rock"),
        POP("Pop"),
        HIP_HOP("Hip-Hop"),
        RAP("Rap"),
        JAZZ("Jazz"),
        BLUES("Blues"),
        CLASSICAL("Classique"),
        ELECTRONIC("Électronique"),
        HOUSE("House"),
        TECHNO("Techno"),
        TRANCE("Trance"),
        DUBSTEP("Dubstep"),
        METAL("Metal"),
        PUNK("Punk"),
        COUNTRY("Country"),
        FOLK("Folk"),
        RNB("R&B"),
        REGGAE("Reggae"),
        DISCO("Disco"),
        FUNK("Funk"),
        SOUL("Soul"),
        GOSPEL("Gospel"),
        LATIN("Latino"),
        SALSA("Salsa"),
        INDIE("Indie"),
        ALTERNATIVE("Alternatif"),
        GRUNGE("Grunge"),
        AMBIENT("Ambient"),
        LOUNGE("Lounge"),
        DNB("Drum & Bass"),
        SKA("Ska"),
        EDM("EDM"),
        SYNTHPOP("Synthpop"),
        K_POP("K-Pop"),
        J_POP("J-Pop"),
        BOSSA_NOVA("Bossa Nova"),
        SWING("Swing"),
        AFROBEAT("Afrobeat"),
        TRAP("Trap"),
        LO_FI("Lo-Fi"),
        CHIPTUNE("Chiptune"),
        VAPORWAVE("Vaporwave"),
        SHOEGAZE("Shoegaze"),
        POST_ROCK("Post-Rock"),
        PROGRESSIVE_ROCK("Rock Progressif"),
        GARAGE("UK Garage"),
        GRIME("Grime"),
        DRILL("Drill"),
        CITY_POP("City Pop"),
        NEOSOUL("Neo-Soul");


    private final String label;

    private ProjectMusicalGenderPreDefined(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
