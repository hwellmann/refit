// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.eg.music;

public class MusicPlayer {

    static Music playing = null;
    static double paused = 0;

    // Controls /////////////////////////////////

    static void play(Music m) {
        if (paused == 0) {
            Music.status = "loading";
            double seconds = m == playing ? 0.3 : 2.5 ;
            Simulator.nextPlayStarted = Simulator.schedule(seconds);
        } else {
            Music.status = "playing";
            Simulator.nextPlayComplete = Simulator.schedule(paused);
            paused = 0;
        }
    }

    static void pause() {
        Music.status = "pause";
        if (playing != null && paused == 0) {
            paused = (Simulator.nextPlayComplete - Simulator.time) / 1000.0;
            Simulator.nextPlayComplete = 0;
        }
    }

    static void stop() {
        Simulator.nextPlayStarted = 0;
        Simulator.nextPlayComplete = 0;
        playComplete();
    }

    // Status ///////////////////////////////////

    static double secondsRemaining() {
        if (paused != 0) {
            return paused;
        } else if (playing != null) {
            return (Simulator.nextPlayComplete - Simulator.time) / 1000.0;
        } else {
            return 0;
        }
    }

    static double minutesRemaining() {
        return Math.round(secondsRemaining() / .6) / 100.0;
    }

    // Events ///////////////////////////////////

    static void playStarted() {
        Music.status = "playing";
        playing = MusicLibrary.looking;
        Simulator.nextPlayComplete = Simulator.schedule(playing.seconds);
    }

    static void playComplete() {
        Music.status = "ready";
        playing = null;
    }
}
