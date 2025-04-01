package com.eql.cda.track.flow.dto.accessDto;

import com.eql.cda.track.flow.entity.AnnotationRight;

import java.util.Date;

public class AccessCreateDTO {

    private Long listenerId;
    private Long versionId;
    private AnnotationRight annotationRight;
    private Date endOfPermission;


    public AccessCreateDTO(Long listenerId, Long versionId, AnnotationRight annotationRight, Date endOfPermission) {
        this.listenerId = listenerId;
        this.versionId = versionId;
        this.annotationRight = annotationRight;
        this.endOfPermission = endOfPermission;
    }


    public Long getListenerId() { return listenerId; }
    public Long getVersionId() { return versionId; }
    public AnnotationRight getAnnotationRight() { return annotationRight; }
    public Date getEndOfPermission() { return endOfPermission; }


    public void setListenerId(Long listenerId) { this.listenerId = listenerId; }
    public void setVersionId(Long versionId) { this.versionId = versionId; }
    public void setAnnotationRight(AnnotationRight annotationRight) { this.annotationRight = annotationRight; }
    public void setEndOfPermission(Date endOfPermission) { this.endOfPermission = endOfPermission; }
}


