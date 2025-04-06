package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Branch;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.entity.Version;
import com.eql.cda.track.flow.service.implementation.VersionNamingServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
     * Trouve la toute dernière version (celle avec le nom V.M.m le plus élevé)
     * sur la branche nommée 'main' pour une composition spécifique.
     * Utilise Pageable pour récupérer uniquement le premier résultat après tri.
     *
     * @param compositionId L'ID de la composition parente.
     * @param pageable      Un Pageable configuré pour récupérer le premier élément (ex: PageRequest.of(0, 1)).
     * @return Une Page contenant au plus la dernière version trouvée.
     */
    @Query("SELECT v FROM Version v " +
            "JOIN v.branch b " + // Jointure explicite vers Branch
            "WHERE b.composition.id = :compositionId " + // Filtre sur l'ID de la composition via la branche
            "AND b.name = '" + VersionNamingServiceImpl.MAIN_BRANCH_NAME + "' " + // Filtre sur le nom de la branche 'main'
            "ORDER BY v.name DESC") // Trie par nom de version décroissant (fonctionne pour V.M.m tant que M < 10)
    Page<Version> findLatestVersionOnMainBranchPageable(@Param("compositionId") Long compositionId, Pageable pageable);
    /**
     * Méthode de commodité pour trouver la dernière version sur la branche 'main'
     * pour une composition donnée, sans avoir à manipuler Pageable directement.
     *
     * @param compositionId L'ID de la composition parente.
     * @return Un Optional contenant la dernière version si trouvée, sinon Optional.empty().
     */
    default Optional<Version> findLatestVersionOnMainBranch(Long compositionId) {
        // Crée un Pageable pour demander uniquement le premier résultat (page 0, taille 1)
        Pageable firstResult = PageRequest.of(0, 1);
        // Appelle la méthode JPQL paginée
        Page<Version> latestVersionPage = findLatestVersionOnMainBranchPageable(compositionId, firstResult);
        // Extrait le premier élément de la page, s'il existe
        return latestVersionPage.get().findFirst();
    }




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
