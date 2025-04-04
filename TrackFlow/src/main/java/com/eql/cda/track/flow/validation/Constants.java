package com.eql.cda.track.flow.validation;

import java.util.regex.Pattern;

public final class Constants {



    public static final int PROJECT_TITLE_MAX_LENGTH = 255;
    public static final String PROJECT_TITLE_MAX_LENGTH_MSG = "Project title cannot exceed " + PROJECT_TITLE_MAX_LENGTH + " characters";

    public static final int PROJECT_DESC_MAX_LENGTH = 5000;
    public static final String PROJECT_DESC_MAX_LENGTH_MSG = "Project description cannot exceed " + PROJECT_DESC_MAX_LENGTH + " characters";

    public static final int COMPOSITION_TITLE_MAX_LENGTH = 255;
    public static final String COMPOSITION_TITLE_MAX_LENGTH_MSG = "Composition title cannot exceed " + COMPOSITION_TITLE_MAX_LENGTH + " characters";

    public static final int COMPOSITION_DESC_MAX_LENGTH = 5000;
    public static final String COMPOSITION_DESC_MAX_LENGTH_MSG = "Composition description cannot exceed " + COMPOSITION_DESC_MAX_LENGTH + " characters";


    public static final int RECENT_PROJECT_COUNT = 10;
    public static final int RECENT_COMPOSITION_COUNT = 10;

    public static final Pattern VERSION_NAME_PATTERN = Pattern.compile("^V(\\d+)(?:\\.(\\d+))?(?:\\.(\\d+))?$");

    // ^       : Doit commencer exactement au début du texte.
    // V       : Doit y avoir la lettre V majuscule.
    // (\\d+)  : Doit suivre un groupe (les parenthèses '()') composé de :
    //    \\d  : Un chiffre (0-9). (Le double '\\' est requis en Java pour dire 'un seul \').
    //    +    : Le chiffre doit apparaître 1 fois OU PLUSIEURS fois (ex: "1", "12", "345").
    //         -> C'est le 1er groupe capturant (pour le numéro majeur).
    // \\.     : Doit y avoir un point littéral '.' (Le '\\' est là car '.' seul a un sens spécial en regex).
    // (\\d+)  : Doit suivre un deuxième groupe composé de chiffres (1 ou plusieurs).
    //         -> C'est le 2ème groupe capturant (pour le numéro mineur).
    // $       : Doit se terminer exactement ici. Pas d'autres caractères après.
}
