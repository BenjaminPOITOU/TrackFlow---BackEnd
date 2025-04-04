package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnotationRepository extends JpaRepository <Annotation, Long> {

    List<Annotation> findByVersion(Version version);
}
