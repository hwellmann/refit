// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.glassfish.eg.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class MusicLibrary {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private MusicPlayer musicPlayer;
    
    @Inject
    private Simulator simulator;
    
    private List<Music> queryResults;

    public void load(String name) throws Exception {
        em.createQuery("delete from Music m").executeUpdate();
        
        File file = new File(System.getProperty("fit.inputDir"), name);
        BufferedReader in = new BufferedReader(new FileReader(file));
        in.readLine(); // skip column headings
        int id = 1;
        while (in.ready()) {
            Music music = Music.parse(in.readLine());
            music.setId(id++);
            em.persist(music);
        }
        in.close();
    }

    public Music select(int id) {
        Music music = em.find(Music.class, id);
        return music;
    }

    public void search(double seconds){
        musicPlayer.setStatus("searching");
        simulator.nextSearchComplete = simulator.schedule(seconds);
    }
    public void findAll() {
        search(3.2);
        queryResults = em.createQuery("select m from Music m", Music.class).getResultList();
    }
    
    public int getTotalSize() {
        long size = em.createQuery("select count(m) from Music m", Long.class).getSingleResult();
        return (int)size;
        
    }

    public void findArtist(String a) {
        search(2.3);
        String jpql = "select m from Music m where m.artist = :artist";
        TypedQuery<Music> query = em.createQuery(jpql, Music.class);
        query.setParameter("artist", a);
        queryResults = query.getResultList();
    }

    public void findAlbum(String a) {
        search(1.1);
        String jpql = "select m from Music m where m.album = :album";
        TypedQuery<Music> query = em.createQuery(jpql, Music.class);
        query.setParameter("album", a);
        queryResults = query.getResultList();
    }

    public void findGenre(String a) {
        search(0.2);
        String jpql = "select m from Music m where m.genre = :genre";
        TypedQuery<Music> query = em.createQuery(jpql, Music.class);
        query.setParameter("genre", a);
        queryResults = query.getResultList();
    }

    public void findYear(int a) {
        search(0.8);
        String jpql = "select m from Music m where m.year = :year";
        TypedQuery<Music> query = em.createQuery(jpql, Music.class);
        query.setParameter("year", a);
        queryResults = query.getResultList();
    }

    public int displayCount() {
        if (queryResults == null) {
            findAll();
        }
        return queryResults.size();
    }

    public Music[] displayContents () {
        return queryResults.toArray(new Music[displayCount()]);
    }

}
