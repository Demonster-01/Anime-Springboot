package com.Suresh.Anime.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class AnimeDto {
    @NotEmpty(message = "The name is required")
    private String name;

    @NotEmpty(message = "The genre is required")
    private String genre;

    private String director;

    private String studio;

    @Min(value = 0, message = "The episode count cannot be negative")
    private float episode;

//    @Size(min = 10, message = "The description should be at least 10 characters")
//    @Size(max = 2000, message = "The description cannot exceed 2000 characters")
//    private String description;

    private MultipartFile imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public float getEpisode() {
        return episode;
    }

    public void setEpisode(float episode) {
        this.episode = episode;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
