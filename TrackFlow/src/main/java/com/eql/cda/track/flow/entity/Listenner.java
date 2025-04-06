package com.eql.cda.track.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.checkerframework.common.aliasing.qual.Unique;


import java.time.Instant;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "listenners")
public class Listenner extends User {



    public Listenner() {
    }

    public Listenner(Long id, String lastName, String firstName, @Unique String login, String password, Instant creationDate, Instant updateDate, Instant suppressionDate, List<Project> projects, UserRole userRole, Set<Access> accesses, Long id1) {
        super(id, lastName, firstName, login, password, creationDate, updateDate, suppressionDate, projects, userRole, accesses);
    }
}
