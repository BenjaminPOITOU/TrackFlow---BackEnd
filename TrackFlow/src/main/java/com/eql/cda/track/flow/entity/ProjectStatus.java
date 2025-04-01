package com.eql.cda.track.flow.entity;

public enum ProjectStatus {
    EN_COURS("En cours"),
    FINALISE("Finalisé"),
    EN_PAUSE("En pause");

    private final String label;

    private ProjectStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
