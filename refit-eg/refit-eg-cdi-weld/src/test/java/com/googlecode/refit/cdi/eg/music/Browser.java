// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.cdi.eg.music;

import javax.inject.Inject;

import fit.Fixture;

public class Browser extends Fixture {

    @Inject
    private MusicLibrary musicLibrary;

    @Inject
    private MusicPlayer musicPlayer;
    
    // Library //////////////////////////////////

    public void library (String path) throws Exception {
        musicLibrary.load(path);
    }

    public int totalSongs() {
        return musicLibrary.library.length;
    }

    // Select Detail ////////////////////////////

    public String playing () {
        return musicPlayer.playing.title;
    }

    public void select (int i) {
        musicLibrary.select(musicLibrary.library[i-1]);
    }

    public String title() {
        return musicLibrary.looking.title;
    }

    public String artist() {
        return musicLibrary.looking.artist;
    }

    public String album() {
        return musicLibrary.looking.album;
    }

    public int year() {
        return musicLibrary.looking.year;
    }

    public double time() {
        return musicLibrary.looking.time();
    }

    public String track() {
        return musicLibrary.looking.track();
    }

    // Search Buttons ///////////////////////////

    public void sameAlbum() {
        musicLibrary.findAlbum(musicLibrary.looking.album);
    }

    public void sameArtist() {
        musicLibrary.findArtist(musicLibrary.looking.artist);
    }

    public void sameGenre() {
        musicLibrary.findGenre(musicLibrary.looking.genre);
    }

    public void sameYear() {
        musicLibrary.findYear(musicLibrary.looking.year);
    }

    public int selectedSongs() {
        return musicLibrary.displayCount();
    }

    public void showAll() {
        musicLibrary.findAll();
    }

    // Play Buttons /////////////////////////////

    public void play() {
        musicPlayer.play(musicLibrary.looking);
    }

    public void pause() {
        musicPlayer.pause();
    }

    public String status() {
        return Music.status;
    }

    public double remaining() {
        return musicPlayer.minutesRemaining();
    }

}
