package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository <Version, Long> {
}
