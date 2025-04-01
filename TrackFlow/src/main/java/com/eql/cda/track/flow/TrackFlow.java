package com.eql.cda.track.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrackFlow {
    public static void main(String[] args) {
        SpringApplication.run(TrackFlow.class, args);
    }
}
