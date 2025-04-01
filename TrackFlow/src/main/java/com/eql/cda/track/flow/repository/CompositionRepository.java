package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Composition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompositionRepository extends JpaRepository <Composition, Long> {
}
