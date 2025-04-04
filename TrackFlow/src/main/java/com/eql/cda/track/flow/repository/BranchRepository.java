package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByCompositionId(Long compositionId);
    List<Branch> findByCompositionIdOrderByNameAsc(Long compositionId);

}
