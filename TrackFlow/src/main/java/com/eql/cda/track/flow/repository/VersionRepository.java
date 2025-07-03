package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Version} entity.
 */
@Repository
public interface VersionRepository extends JpaRepository<Version, Long> {

    List<Version> findByBranchIdOrderByCreatedDateDesc(Long branchId);

    @Query("SELECT v FROM Version v " +
            "LEFT JOIN FETCH v.instruments " +
            "LEFT JOIN FETCH v.annotations " +
            "WHERE v.id = :versionId")
    Optional<Version> findByIdWithDetails(@Param("versionId") Long versionId);

    /**
     * Finds the most recent version in a specific branch.
     *
     * @param branchId The ID of the branch.
     * @return An {@link Optional} containing the latest {@link Version}.
     */
    Optional<Version> findFirstByBranchIdOrderByCreatedDateDesc(Long branchId);
}