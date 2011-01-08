// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.glassfish.eg.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicLibrary {
    
    @Inject
    private MusicPlayer musicPlayer;
    
    @Inject
    private Simulator simulator;
    
    Music looking = null;
    Music library[] = {};

    void load(String name) throws Exception {
        List<Music> music = new ArrayList<Music>();
        File file = new File(System.getProperty("fit.inputDir"), name);
        BufferedReader in = new BufferedReader(new FileReader(file));
        in.readLine(); // skip column headings
        while(in.ready()) {
            music.add(Music.parse(in.readLine()));
        }
        in.close();
        library = (Music[])music.toArray(library);
    }

    void select(Music m) {
        looking = m;
    }

    void search(double seconds){
        Music.status = "searching";
        simulator.nextSearchComplete = simulator.schedule(seconds);
    }

    void searchComplete() {
        Music.status = musicPlayer.playing == null ? "ready" : "playing";
    }

    void findAll() {
        search(3.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = true;
        }
    }

    void findArtist(String a) {
        search(2.3);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].artist.equals(a);
        }
    }

    void findAlbum(String a) {
        search(1.1);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].album.equals(a);
        }
    }

    void findGenre(String a) {
        search(0.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].genre.equals(a);
        }
    }

    void findYear(int a) {
        search(0.8);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].year == a;
        }
    }

    int displayCount() {
        int count = 0;
        for (int i=0; i<library.length; i++) {
            count += (library[i].selected ? 1 : 0);
        }
        return count;
    }

    Music[] displayContents () {
        Music displayed[] = new Music[displayCount()];
        for (int i=0, j=0; i<library.length; i++) {
            if (library[i].selected) {
                displayed[j++] = library[i];
            }
        }
        return displayed;
    }

}
