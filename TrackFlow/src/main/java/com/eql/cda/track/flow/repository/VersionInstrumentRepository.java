package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.entity.VersionInstrument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionInstrumentRepository extends JpaRepository<VersionInstrument, Long> {

    List<VersionInstrument> findByVersion(Version version);
}
