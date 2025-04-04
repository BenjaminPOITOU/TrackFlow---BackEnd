package com.eql.cda.track.flow.dto.versionDto;

import com.eql.cda.track.flow.entity.VersionInstrumentPreDefined;

public class VersionInstrumentDto {


    private String value; // Le nom technique (GUITARE)
    private String label; // Le nom lisible (Guitare)

    public VersionInstrumentDto(VersionInstrumentPreDefined instrumentEnum) {
        if (instrumentEnum != null) {
            this.value = instrumentEnum.name();
            this.label = instrumentEnum.getLabel();
        }
    }
    // Getters...
    public String getValue() { return value; }
    public String getLabel() { return label; }
}
