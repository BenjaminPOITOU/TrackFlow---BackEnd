package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);
    List<Project> findAll();
    List<Project> findByUserId(Long userId);

    @Query("SELECT p FROM Project p WHERE p.user.id = :userId")
    List<Project> findByUserIdWithFullDetails(@Param("userId") Long userId);

    //@Query("SELECT DISTINCT p FROM Project p " +
            //"LEFT JOIN FETCH p.user u " +         // Garder le fetch User
            //"LEFT JOIN FETCH p.compositions c " + // Garder le fetch Compositions
            // "LEFT JOIN FETCH p.projectPurposes pp " + // <-- COMMENTÉ/SUPPRIMÉ
            // "LEFT JOIN FETCH p.projectMusicalGendersPreDefined pmgp " + // <-- COMMENTÉ/SUPPRIMÉ
           // "WHERE u.id = :userId")
    //List<Project> findByUserIdWithFullDetails(@Param("userId") Long userId);



}
