// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.osgi.eg.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.googlecode.refit.osgi.eg.music.Simulator;

public class MusicLibrary {
    
    private static DateFormat dateFormat = new SimpleDateFormat("M/d/yy h:mm a");
    
    private MusicPlayer musicPlayer;
    
    private Simulator simulator;

    private Music library[] = {};
    
    
    protected void setMusicPlayer(MusicPlayer player) {
        this.musicPlayer = player;
    }
    
    protected void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
    
    public void load(String name) throws Exception {
        List<Music> music = new ArrayList<Music>();
        File file = new File(System.getProperty("fit.inputDir"), name);
        BufferedReader in = new BufferedReader(new FileReader(file));
        in.readLine(); // skip column headings
        while(in.ready()) {
            music.add(parse(in.readLine()));
        }
        in.close();
        library = (Music[])music.toArray(library);
    }
    

    private Music parse(String string) throws ParseException {
        Music m = new Music();
        StringTokenizer t = new StringTokenizer(string,"\t");
        m.title =       t.nextToken();
        m.artist =      t.nextToken();
        m.album =       t.nextToken();
        m.genre =       t.nextToken();
        m.size =        Long.parseLong(t.nextToken());
        m.seconds =     Integer.parseInt(t.nextToken());
        m.trackNumber = Integer.parseInt(t.nextToken());
        m.trackCount =  Integer.parseInt(t.nextToken());
        m.year =        Integer.parseInt(t.nextToken());
        m.date =        dateFormat.parse(t.nextToken());
        return m;
    }
    
    public Date parseDate(String s) throws ParseException {
        return dateFormat.parse(s);
    }
    

    public Music select(int id) {
        Music music = library[id-1];
        music.selected = true;
        return music;
    }

    public void search(double seconds){
        musicPlayer.setStatus("searching");
        simulator.nextSearchComplete = simulator.schedule(seconds);
    }
    
    public void findAll() {
        search(3.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = true;
        }
    }
    
    public int getTotalSize() {
        return library.length;
        
    }

    public void findArtist(String a) {
        search(2.3);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].artist.equals(a);
        }
    }

    public void findAlbum(String a) {
        search(1.1);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].album.equals(a);
        }
    }

    public void findGenre(String a) {
        search(0.2);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].genre.equals(a);
        }
    }

    public void findYear(int a) {
        search(0.8);
        for (int i=0; i<library.length; i++) {
            library[i].selected = library[i].year == a;
        }
    }

    public int displayCount() {
        int count = 0;
        for (int i=0; i<library.length; i++) {
            count += (library[i].selected ? 1 : 0);
        }
        return count;
    }

    public Music[] displayContents () {
        Music displayed[] = new Music[displayCount()];
        for (int i=0, j=0; i<library.length; i++) {
            if (library[i].selected) {
                displayed[j++] = library[i];
            }
        }
        return displayed;
    }
}
