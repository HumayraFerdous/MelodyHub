package com.melodyhub.service;

import com.melodyhub.model.Song;
import com.melodyhub.exception.SongNotFoundException;
import java.util.*;

public class MusicPlayer {
    private Stack<Song> history = new Stack<>();

    public void playSong(Song song) {
        history.push(song);
        System.out.println("Now playing: " + song);
    }

    public Song getLastPlayed() throws SongNotFoundException {
        if (history.isEmpty()) {
            throw new SongNotFoundException("No song played yet!");
        }
        return history.peek();
    }
}
