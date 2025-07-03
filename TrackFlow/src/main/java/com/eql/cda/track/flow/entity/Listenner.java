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


}
