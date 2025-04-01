package com.eql.cda.track.flow.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "musicians")
public class Musician extends User{


    private String address;
    private String mobile;
    private String biography;
    private String picture;

    @OneToMany(mappedBy = "musician", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    public Musician() {
    }


    public Musician(List<Playlist> playlists) {
        this.playlists = playlists;
    }


    public Musician(Long id, String lastName, String firstName, @Unique String login, String password, LocalDateTime creationDate, LocalDateTime updateDate, LocalDateTime suppressionDate, List<Project> projects, UserRole userRole, Set<Access> accesses, String address, String mobile, String biography, String picture, List<Playlist> playlists) {
        super(id, lastName, firstName, login, password, creationDate, updateDate, suppressionDate, projects, userRole, accesses);
        this.address = address;
        this.mobile = mobile;
        this.biography = biography;
        this.picture = picture;
        this.playlists = playlists;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }
    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }


}
