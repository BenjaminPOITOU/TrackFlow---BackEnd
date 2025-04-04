package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository <Version, Long> {


    /**
     * Trouve la dernière version (basée sur l'ID descendant, qui correspond généralement
     *      * à l'ordre de création) pour une branche spécifique.
     * Utilisé pour déterminer le numéro de la version suivante sur cette branche.
     * @param branch La branche pour laquelle chercher la dernière version.
     * @return Un Optional contenant la dernière version si elle existe, sinon Optional.empty().
     */
    Optional<Version> findTopByBranchOrderByIdDesc(Branch branch);
    List<Version> findByBranchId(Long branchId);
    List<Version> findByBranch_Composition_Id(Long compositionId);
}
