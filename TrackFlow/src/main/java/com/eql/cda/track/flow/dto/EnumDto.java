package com.eql.cda.track.flow.dto;


public class EnumDto {
    private String value; // La valeur technique (nom de l'enum constant)
    private String label; // Le label lisible par l'utilisateur

    public EnumDto(String value, String label) {
        this.value = value;
        this.label = label;
    }


    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }


}