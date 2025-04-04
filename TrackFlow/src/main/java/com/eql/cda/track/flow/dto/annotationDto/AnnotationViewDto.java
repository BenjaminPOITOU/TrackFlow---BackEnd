package com.eql.cda.track.flow.dto.annotationDto;

public class AnnotationViewDto {

    private Long id;
    private String text;
    private Float timePosition; // ou String, ou autre type
    private String type; // exemple

    public AnnotationViewDto(Long id, String text, Float timePosition, String type) {
        this.id = id;
        this.text = text;
        this.timePosition = timePosition;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getTimePosition() {
        return timePosition;
    }

    public void setTimePosition(Float timePosition) {
        this.timePosition = timePosition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
