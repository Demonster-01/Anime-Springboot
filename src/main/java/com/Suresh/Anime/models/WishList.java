package com.Suresh.Anime.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="wishList")
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String Genre;
    private String Director;
    private String Studio;
    private double Eposide;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Date Aired;
    private String imageFilename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public String getStudio() {
        return Studio;
    }

    public void setStudio(String studio) {
        Studio = studio;
    }

    public double getEposide() {
        return Eposide;
    }

    public void setEposide(double eposide) {
        Eposide = eposide;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAired() {
        return Aired;
    }

    public void setAired(Date aired) {
        Aired = aired;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
}