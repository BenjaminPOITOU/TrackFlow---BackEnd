package com.eql.cda.track.flow.dto;


public class EnumDto {
    private String value;
    private String label;

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