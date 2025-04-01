package com.eql.cda.track.flow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "instruments_chosen_user")
public class InstrumentChosenByUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userInstrumentAdded;
    private String color;

    @ManyToOne
    @JoinColumn(name = "version_id")
    private Version version;


    public InstrumentChosenByUser() {
    }

    public InstrumentChosenByUser(Long id, String userInstrumentAdded, String color, Version version) {
        this.id = id;
        this.userInstrumentAdded = userInstrumentAdded;
        this.color = color;
        this.version = version;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserInstrumentAdded() {
        return userInstrumentAdded;
    }
    public void setUserInstrumentAdded(String userInstrumentAdded) {
        this.userInstrumentAdded = userInstrumentAdded;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Version getVersion() {
        return version;
    }
    public void setVersion(Version version) {
        this.version = version;
    }
}
