package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Annotation;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.entity.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnnotationRepository extends JpaRepository <Annotation, Long> {

    List<Annotation> findByVersion(Version version);
    List<Annotation> findByVersionId(Long versionId);

    @Query(value = "SELECT a FROM Annotation a " +
            "LEFT JOIN FETCH a.version v " +
            "LEFT JOIN FETCH v.branch b " +
            "LEFT JOIN FETCH b.composition c " +
            // --- CORRECTION : Ajout de la jointure vers Project ---
            "JOIN c.project p " + // Jointure simple, pas besoin de FETCH project pour le DTO normalement
            "WHERE p.user = :user", // Filtre sur l'utilisateur du PROJET
            countQuery = "SELECT count(a) FROM Annotation a " +
                    "JOIN a.version v " +
                    "JOIN v.branch b " +
                    "JOIN b.composition c " +
                    // --- CORRECTION : Ajout de la jointure vers Project ---
                    "JOIN c.project p " +
                    "WHERE p.user = :user")
    Page<Annotation> findRecentAnnotationsByUserWithDetails(User user, Pageable pageable);
    List<Annotation> findByVersionIdOrderByCreationDateDesc(Long versionId);

}
