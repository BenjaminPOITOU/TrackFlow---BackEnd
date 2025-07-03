package com.eql.cda.track.flow.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Represents a Musician user.
 * This entity inherits all role information from the base User class
 * and adds musician-specific attributes like biography and playlists.
 */
@Entity
@Table(name = "musicians")
@PrimaryKeyJoinColumn(name = "user_id")
public class Musician extends User {

    private String address;
    private String mobile;
    private String biography;
    private String picture;

    @OneToMany(mappedBy = "musician", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    public Musician() {
        super();
    }

    // Getters and setters for musician-specific fields...
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public List<Playlist> getPlaylists() { return playlists; }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }
}