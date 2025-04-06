package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Vérifie si au moins une version existe pour une composition donnée.
     * @param compositionId L'ID de la composition.
     * @return true si au moins une version existe, false sinon.
     */
    boolean existsByBranch_Composition_Id(Long compositionId);

    /**
     * Vérifie si au moins une version existe sur une branche spécifique d'une composition donnée.
     * @param compositionId L'ID de la composition.
     * @param branchName Le nom de la branche (ex: "main").
     * @return true si au moins une version existe sur cette branche, false sinon.
     */
    boolean existsByBranch_Composition_IdAndBranch_Name(Long compositionId, String branchName);

    @Query(value = "SELECT v FROM Version v " +
            "JOIN v.branch b " +
            "JOIN b.composition c " +
            "JOIN c.project p " +
            "WHERE p.user = :user " +
            "ORDER BY v.createdDate DESC",
            countQuery = "SELECT COUNT(v) FROM Version v " + // Count query nécessaire pour Page
                    "JOIN v.branch b " +
                    "JOIN b.composition c " +
                    "JOIN c.project p " +
                    "WHERE p.user = :user")
    Page<Version> findLatestVersionForUser(@Param("user") User user, Pageable pageable);
}
