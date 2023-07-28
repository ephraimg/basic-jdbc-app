package com.ephraimglick.basicjdbcapp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "artists", schema = "music")
public class Artist {
    @Id
    @Column(name = "artist_name")
    public String artistName;

    public int rating;

    public Artist() {
    }

    public Artist(String artistName, int rating) {
        this.artistName = artistName;
        this.rating = rating;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
