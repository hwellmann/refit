// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.eg.music;

import fit.*;

public class Browser extends Fixture {


    // Library //////////////////////////////////

    public void library (String path) throws Exception {
        MusicLibrary.load(path);
    }

    public int totalSongs() {
        return MusicLibrary.library.length;
    }

    // Select Detail ////////////////////////////

    public String playing () {
        return MusicPlayer.playing.title;
    }

    public void select (int i) {
        MusicLibrary.select(MusicLibrary.library[i-1]);
    }

    public String title() {
        return MusicLibrary.looking.title;
    }

    public String artist() {
        return MusicLibrary.looking.artist;
    }

    public String album() {
        return MusicLibrary.looking.album;
    }

    public int year() {
        return MusicLibrary.looking.year;
    }

    public double time() {
        return MusicLibrary.looking.time();
    }

    public String track() {
        return MusicLibrary.looking.track();
    }

    // Search Buttons ///////////////////////////

    public void sameAlbum() {
        MusicLibrary.findAlbum(MusicLibrary.looking.album);
    }

    public void sameArtist() {
        MusicLibrary.findArtist(MusicLibrary.looking.artist);
    }

    public void sameGenre() {
        MusicLibrary.findGenre(MusicLibrary.looking.genre);
    }

    public void sameYear() {
        MusicLibrary.findYear(MusicLibrary.looking.year);
    }

    public int selectedSongs() {
        return MusicLibrary.displayCount();
    }

    public void showAll() {
        MusicLibrary.findAll();
    }

    // Play Buttons /////////////////////////////

    public void play() {
        MusicPlayer.play(MusicLibrary.looking);
    }

    public void pause() {
        MusicPlayer.pause();
    }

    public String status() {
        return Music.status;
    }

    public double remaining() {
        return MusicPlayer.minutesRemaining();
    }

}
