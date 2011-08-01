// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.eg.music;

import java.io.*;
import java.util.*;

public class MusicLibrary {
    static Music looking = null;
    static Music library[] = {};

    static void load(String name) throws Exception {
        List<Music> music = new ArrayList<Music>();
        File file = new File(System.getProperty("fit.inputDir"), name);
        BufferedReader in = new BufferedReader(new FileReader(file));
        in.readLine(); // skip column headings
        while(in.ready()) {
            music.add(Music.parse(in.readLine()));
        }
        in.close();
        library = music.toArray(library);
    }

    static void select(Music m) {
        looking = m;
    }

    static void search(double seconds){
        Music.status = "searching";
        Simulator.nextSearchComplete = Simulator.schedule(seconds);
    }

    static void searchComplete() {
        Music.status = MusicPlayer.playing == null ? "ready" : "playing";
    }

    static void findAll() {
        search(3.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = true;
        }
    }

    static void findArtist(String a) {
        search(2.3);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].artist.equals(a);
        }
    }

    static void findAlbum(String a) {
        search(1.1);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].album.equals(a);
        }
    }

    static void findGenre(String a) {
        search(0.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].genre.equals(a);
        }
    }

    static void findYear(int a) {
        search(0.8);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].year == a;
        }
    }

    static int displayCount() {
        int count = 0;
        for (int i=0; i<library.length; i++) {
            count += (library[i].selected ? 1 : 0);
        }
        return count;
    }

    static Music[] displayContents () {
        Music displayed[] = new Music[displayCount()];
        for (int i=0, j=0; i<library.length; i++) {
            if (library[i].selected) {
                displayed[j++] = library[i];
            }
        }
        return displayed;
    }

}
