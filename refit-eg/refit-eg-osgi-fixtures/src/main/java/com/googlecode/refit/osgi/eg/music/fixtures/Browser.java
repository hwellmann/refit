// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.osgi.eg.music.fixtures;

import com.googlecode.refit.osgi.eg.music.Music;
import com.googlecode.refit.osgi.eg.music.MusicLibrary;
import com.googlecode.refit.osgi.eg.music.MusicPlayer;

import fit.Fixture;


public class Browser extends Fixture {

    private MusicLibrary musicLibrary;

    private MusicPlayer musicPlayer;
    
    private Music looking;

    
    protected void setMusicLibrary(MusicLibrary library) {
        this.musicLibrary = library;
    }
    
    protected void setMusicPlayer(MusicPlayer player) {
        this.musicPlayer = player;
    }
    
    // Library //////////////////////////////////

    public void library (String path) throws Exception {
        musicLibrary.load(path);
    }

    public int totalSongs() {
        return musicLibrary.getTotalSize();
    }

    // Select Detail ////////////////////////////

    public String playing () {
        return musicPlayer.getPlaying().title;
    }

    public void select (int i) {
        looking = musicLibrary.select(i);
    }

    public String title() {
        return looking.title;
    }

    public String artist() {
        return looking.artist;
    }

    public String album() {
        return looking.album;
    }

    public int year() {
        return looking.year;
    }

    public double time() {
        return looking.time();
    }

    public String track() {
        return looking.track();
    }

    // Search Buttons ///////////////////////////

    public void sameAlbum() {
        musicLibrary.findAlbum(looking.album);
    }

    public void sameArtist() {
        musicLibrary.findArtist(looking.artist);
    }

    public void sameGenre() {
        musicLibrary.findGenre(looking.genre);
    }

    public void sameYear() {
        musicLibrary.findYear(looking.year);
    }

    public int selectedSongs() {
        return musicLibrary.displayCount();
    }

    public void showAll() {
        musicLibrary.findAll();
    }

    // Play Buttons /////////////////////////////

    public void play() {
        musicPlayer.play(looking);
    }

    public void pause() {
        musicPlayer.pause();
    }

    public String status() {
        return musicPlayer.getStatus();
    }

    public double remaining() {
        return musicPlayer.minutesRemaining();
    }

}
