// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.javaee.eg.music;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MusicPlayer {
    
    @Inject
    private Simulator simulator;

    private Music playing = null;
    private Music looking;
    private double paused = 0;

    private String status = "ready";

    // Controls /////////////////////////////////

    public void play(Music m) {
        looking = m;
        if (paused == 0) {
            status = "loading";
            double seconds = m == playing ? 0.3 : 2.5 ;
            simulator.setNextPlayStarted(simulator.schedule(seconds));
        } else {
            status = "playing";
            simulator.setNextPlayComplete(simulator.schedule(paused));
            paused = 0;
        }
    }

    public void pause() {
        status = "pause";
        if (playing != null && paused == 0) {
            paused = (simulator.getNextPlayComplete() - simulator.getTime()) / 1000.0;
            simulator.setNextPlayComplete(0);
        }
    }

    public void stop() {
        simulator.setNextPlayStarted(0);
        simulator.setNextPlayComplete(0);
        playComplete();
    }

    // Status ///////////////////////////////////

    public double secondsRemaining() {
        if (paused != 0) {
            return paused;
        } else if (playing != null) {
            return (simulator.getNextPlayComplete() - simulator.getTime()) / 1000.0;
        } else {
            return 0;
        }
    }

    public double minutesRemaining() {
        return Math.round(secondsRemaining() / .6) / 100.0;
    }

    // Events ///////////////////////////////////

    public void playStarted() {
        status = "playing";
        playing = looking;
        simulator.setNextPlayComplete(simulator.schedule(playing.seconds));
    }

    public void playComplete() {
        status = "ready";
        playing = null;
    }
    
    public void searchComplete() {
        setStatus(playing == null ? "ready" : "playing");
    }
    
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Music getPlaying() {
        return playing;
    }
}
