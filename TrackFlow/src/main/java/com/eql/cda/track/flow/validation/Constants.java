package com.eql.cda.track.flow.validation;

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
}
