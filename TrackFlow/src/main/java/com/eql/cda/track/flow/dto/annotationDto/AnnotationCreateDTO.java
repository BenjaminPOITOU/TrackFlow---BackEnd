package com.eql.cda.track.flow.dto.annotationDto;

import com.eql.cda.track.flow.entity.AnnotationCategory;
import com.eql.cda.track.flow.entity.AnnotationStatus;

public class AnnotationCreateDTO {

    private Long versionId; // ID de la version à annoter
    private String content; // Texte de l'annotation
    private Float timePosition; // Moment précis dans le temps (en secondes, par exemple)
    private AnnotationCategory annotationCategory; // Catégorie de l'annotation (ex: MIXAGE)
    private AnnotationStatus annotationStatus = AnnotationStatus.A_TRAITER; // Statut initial (ex: A_TRAITER)

    public AnnotationCreateDTO() {
    }
    public AnnotationCreateDTO(AnnotationStatus annotationStatus, AnnotationCategory annotationCategory, Float timePosition, String content, Long versionId) {
        this.annotationStatus = annotationStatus;
        this.annotationCategory = annotationCategory;
        this.timePosition = timePosition;
        this.content = content;
        this.versionId = versionId;
    }

    public Long getVersionId() {
        return versionId;
    }
    public String getContent() {
        return content;
    }
    public Float getTimePosition() {
        return timePosition;
    }
    public AnnotationCategory getAnnotationCategory() {
        return annotationCategory;
    }
    public AnnotationStatus getAnnotationStatus() {
        return annotationStatus;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setTimePosition(Float timePosition) {
        this.timePosition = timePosition;
    }

}
