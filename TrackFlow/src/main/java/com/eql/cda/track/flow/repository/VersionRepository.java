package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VersionRepository extends JpaRepository <Version, Long> {



    List<Version> findByBranchId(Long branchId);
    List<Version> findByBranch_Composition_Id(Long compositionId);

    @Query("SELECT v FROM Version v JOIN v.branch b WHERE b.name = 'main' ORDER BY v.name DESC")
    Optional<Version> findLatestVersionOnMainBranch();
    Optional<Version> findTopByBranchOrderByNameDesc(Branch targetBranch);
    Optional<Version> findById(Long currentVersionId);
}
