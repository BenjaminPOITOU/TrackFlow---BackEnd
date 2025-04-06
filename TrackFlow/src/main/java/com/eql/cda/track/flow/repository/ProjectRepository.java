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
    Page<Project> findByUser(User user, Pageable pageable);
    @Query("SELECT p FROM Project p WHERE p.user.id = :userId")
    List<Project> findByUserIdWithFullDetails(@Param("userId") Long userId);



}
