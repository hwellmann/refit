// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.osgi.eg.music;

import com.googlecode.refit.osgi.eg.music.EventListener;
import com.googlecode.refit.osgi.eg.music.Simulator;


public class MusicPlayer implements EventListener {
    
    private Simulator simulator;

    private Music playing = null;
    private Music looking;
    double paused = 0;

    private String status = "ready";

    protected void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }
    
    // Controls /////////////////////////////////

    public void play(Music m) {
        looking = m;
        if (paused == 0) {
            status = "loading";
            double seconds = m == playing ? 0.3 : 2.5 ;
            simulator.nextPlayStarted = simulator.schedule(seconds);
        } else {
            status = "playing";
            simulator.nextPlayComplete = simulator.schedule(paused);
            paused = 0;
        }
    }

    public void pause() {
        status = "pause";
        if (playing != null && paused == 0) {
            paused = (simulator.nextPlayComplete - simulator.getTime()) / 1000.0;
            simulator.nextPlayComplete = 0;
        }
    }

    public void stop() {
        simulator.nextPlayStarted = 0;
        simulator.nextPlayComplete = 0;
        playComplete();
    }

    // Status ///////////////////////////////////

    double secondsRemaining() {
        if (paused != 0) {
            return paused;
        } else if (playing != null) {
            return (simulator.nextPlayComplete - simulator.getTime()) / 1000.0;
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
        simulator.nextPlayComplete = simulator.schedule(playing.seconds);
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
    
    void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void loadJammed() {
        // nothing
    }
    
    public Music getPlaying() {
        return playing;
    }
}
