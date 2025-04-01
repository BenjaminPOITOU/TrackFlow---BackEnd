package com.eql.cda.track.flow.repository;

import com.eql.cda.track.flow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLogin(String login);
    Optional<User> findById(String login);
    Boolean existsByLogin(String login);

}
