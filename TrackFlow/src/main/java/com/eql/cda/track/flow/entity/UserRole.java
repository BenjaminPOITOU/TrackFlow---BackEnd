package com.eql.cda.track.flow.entity;

public enum UserRole {

    ARTIST("Artiste"),
    ENGINEER("Ingénieur"),
    PRODUCER("Producteur"),
    MANAGER("Manager"),
    VISITOR("Visiteur");

    private final String label;

    private UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
