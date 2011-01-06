// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.cdi.eg.music;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import fit.ActionFixture;

@Singleton
public class Simulator {
    
    @Inject
    private MusicLibrary musicLibrary;
    
    @Inject
    private MusicPlayer musicPlayer;
    
    @Inject
    private Dialog dialog;
    

    // This discrete event simulator supports three events
    // each of which is open coded in the body of the simulator.

    long time = new Date().getTime();

    public long nextSearchComplete = 0;
    public long nextPlayStarted = 0;
    public long nextPlayComplete = 0;
    
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
        if (time == nextSearchComplete)     {musicLibrary.searchComplete();}
        if (time == nextPlayStarted)        {musicPlayer.playStarted();}
        if (time == nextPlayComplete)       {musicPlayer.playComplete();}
    }

    void advance (long future) {
        while (time < future) {
            time = nextEvent(future);
            perform();
        }
    }

    long schedule(double seconds){
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
        dialog.setMessage("load jamed");
        dialog.setCaller(ActionFixture.actor);
        ActionFixture.actor = dialog;
    }


}
