package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.Project;
import com.eql.cda.track.flow.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);
    List<Project> findAll();
    Page<Project> findByUserId(Long userId, Pageable pageable);
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId")
    Page<Project> findByUserOrderByCreatedDateDesc(User user, Pageable pageable);

    /**
     * Trouve tous les projets NON ARCHIVÉS pour un utilisateur donné.
     * Spring Data JPA dérive la requête WHERE p.user.id = ?1 AND p.archived = false
     * @param userId L'ID de l'utilisateur.
     * @param pageable Les informations de pagination/tri.
     * @return Une page de projets non archivés.
     */
    Page<Project> findByUserIdAndArchivedFalse(Long userId, Pageable pageable);



}
