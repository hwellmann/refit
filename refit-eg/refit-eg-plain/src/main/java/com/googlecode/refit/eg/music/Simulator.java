// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.eg.music;

import fit.*;
import java.util.Date;

public class Simulator {

    // This discrete event simulator supports three events
    // each of which is open coded in the body of the simulator.

    static Simulator system = new Simulator();
    static long time = new Date().getTime();

    public static long nextSearchComplete = 0;
    public static long nextPlayStarted = 0;
    public static long nextPlayComplete = 0;

    long nextEvent(long bound) {
        long result = bound;
        result = sooner(result, nextSearchComplete);
        result = sooner(result, nextPlayStarted);
        result = sooner(result, nextPlayComplete);
        return result;
    }

    long sooner (long soon, long event) {
        return event > time && event < soon ? event : soon;
    }

    void perform() {
        if (time == nextSearchComplete)     {MusicLibrary.searchComplete();}
        if (time == nextPlayStarted)        {MusicPlayer.playStarted();}
        if (time == nextPlayComplete)       {MusicPlayer.playComplete();}
    }

    void advance (long future) {
        while (time < future) {
            time = nextEvent(future);
            perform();
        }
    }

    static long schedule(double seconds){
        return time + (long)(1000 * seconds);
    }

    void delay (double seconds) {
        advance(schedule(seconds));
    }

    public void waitSearchComplete() {
        advance(nextSearchComplete);
    }

    public void waitPlayStarted() {
        advance(nextPlayStarted);
    }

    public void waitPlayComplete() {
        advance(nextPlayComplete);
    }

    public void failLoadJam() {
        ActionFixture.actor = new Dialog("load jamed", ActionFixture.actor);
    }

    public static Simulator reset() {
        time = new Date().getTime();
        system = new Simulator();
        return system;
    }

}
