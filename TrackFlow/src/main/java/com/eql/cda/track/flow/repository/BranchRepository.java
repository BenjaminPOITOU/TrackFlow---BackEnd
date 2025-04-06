package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.eql.cda.track.flow.service.implementation.VersionNamingServiceImpl.MAIN_BRANCH_NAME;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByCompositionId(Long compositionId);
    List<Branch> findByCompositionIdOrderByNameAsc(Long compositionId);

    List<Branch> findByCompositionId(Long compositionId);

    Optional<Branch> findByCompositionIdAndName(Long compositionId, String branchName);

}
