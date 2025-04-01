package com.eql.cda.track.flow.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "musician_id")
    private Musician musician;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
    private List<PlaylistVersion> playlistVersions;

    public Playlist() {
    }
    public Playlist(String title, String description, Date creationDate, Musician musician, List<PlaylistVersion> playlistVersions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.musician = musician;
        this.playlistVersions = playlistVersions;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Musician getMusician() {
        return musician;
    }
    public void setMusician(Musician musician) {
        this.musician = musician;
    }

    public List<PlaylistVersion> getPlaylistVersions() {
        return playlistVersions;
    }
    public void setPlaylistVersions(List<PlaylistVersion> playlistVersions) {
        this.playlistVersions = playlistVersions;
    }
}
