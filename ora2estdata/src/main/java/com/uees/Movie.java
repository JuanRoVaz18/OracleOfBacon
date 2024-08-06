package com.uees;

public class Movie {
    private String actor;
    private String actorID;
    private String film;
    private String filmID;

    // Constructor que acepta todos los campos
    public Movie(String actor, String actorID, String film, String filmID) {
        this.actor = actor;
        this.actorID = actorID;
        this.film = film;
        this.filmID = filmID;
    }

    // Getters
    public String getActor() {
        return actor;
    }

    public String getActorID() {
        return actorID;
    }

    public String getFilm() {
        return film;
    }

    public String getFilmID() {
        return filmID;
    }

    // Setters
    public void setActor(String actor) {
        this.actor = actor;
    }

    public void setActorID(String actorID) {
        this.actorID = actorID;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public void setFilmID(String filmID) {
        this.filmID = filmID;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "actor='" + actor + '\'' +
                ", actorID='" + actorID + '\'' +
                ", film='" + film + '\'' +
                ", filmID='" + filmID + '\'' +
                '}';
    }
}
