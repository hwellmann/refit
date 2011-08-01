// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.cdi.eg.music;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlayer {
    
    @Inject
    private MusicLibrary musicLibrary;
    
    @Inject
    private Simulator simulator;

    Music playing = null;
    double paused = 0;

    // Controls /////////////////////////////////

    void play(Music m) {
        if (paused == 0) {
            Music.status = "loading";
            double seconds = m == playing ? 0.3 : 2.5 ;
            simulator.nextPlayStarted = simulator.schedule(seconds);
        } else {
            Music.status = "playing";
            simulator.nextPlayComplete = simulator.schedule(paused);
            paused = 0;
        }
    }

    void pause() {
        Music.status = "pause";
        if (playing != null && paused == 0) {
            paused = (simulator.nextPlayComplete - simulator.time) / 1000.0;
            simulator.nextPlayComplete = 0;
        }
    }

    void stop() {
        simulator.nextPlayStarted = 0;
        simulator.nextPlayComplete = 0;
        playComplete();
    }

    // Status ///////////////////////////////////

    double secondsRemaining() {
        if (paused != 0) {
            return paused;
        } else if (playing != null) {
            return (simulator.nextPlayComplete - simulator.time) / 1000.0;
        } else {
            return 0;
        }
    }

    double minutesRemaining() {
        return Math.round(secondsRemaining() / .6) / 100.0;
    }

    // Events ///////////////////////////////////

    void playStarted() {
        Music.status = "playing";
        playing = musicLibrary.looking;
        simulator.nextPlayComplete = simulator.schedule(playing.seconds);
    }

    void playComplete() {
        Music.status = "ready";
        playing = null;
    }
}
