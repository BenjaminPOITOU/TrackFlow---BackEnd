package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnotationRepository extends JpaRepository <Annotation, Long> {
}
