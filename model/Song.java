package com.melodyhub.model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private String genre;
    private String path;

    public Song(int id, String title, String artist, String genre, String path) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.path = path;
    }

    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getGenre() { return genre; }
    public String getPath() { return path; }

    @Override
    public String toString() {
        return title + " by " + artist + " [" + genre + "]";
    }
}
