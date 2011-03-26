// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.glassfish.eg.music;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlayer {
    
    @Inject
    private Simulator simulator;

    Music playing = null;
    private Music looking;
    double paused = 0;

    private String status = "ready";

    // Controls /////////////////////////////////

    void play(Music m) {
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

    void pause() {
        status = "pause";
        if (playing != null && paused == 0) {
            paused = (simulator.getNextPlayComplete() - simulator.getTime()) / 1000.0;
            simulator.setNextPlayComplete(0);
        }
    }

    void stop() {
        simulator.setNextPlayStarted(0);
        simulator.setNextPlayComplete(0);
        playComplete();
    }

    // Status ///////////////////////////////////

    double secondsRemaining() {
        if (paused != 0) {
            return paused;
        } else if (playing != null) {
            return (simulator.getNextPlayComplete() - simulator.getTime()) / 1000.0;
        } else {
            return 0;
        }
    }

    double minutesRemaining() {
        return Math.round(secondsRemaining() / .6) / 100.0;
    }

    // Events ///////////////////////////////////

    void playStarted() {
        status = "playing";
        playing = looking;
        simulator.setNextPlayComplete(simulator.schedule(playing.seconds));
    }

    void playComplete() {
        status = "ready";
        playing = null;
    }
    
    void searchComplete() {
        setStatus(playing == null ? "ready" : "playing");
    }
    
    
    String getStatus() {
        return status;
    }
    
    void setStatus(String status) {
        this.status = status;
    }
}
