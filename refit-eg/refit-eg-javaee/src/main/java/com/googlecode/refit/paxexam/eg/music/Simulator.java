// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.paxexam.eg.music;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fit.ActionFixture;

@ApplicationScoped
public class Simulator {
    
    @Inject
    private MusicPlayer musicPlayer;
    
    @Inject
    private Dialog dialog;
    

    // This discrete event simulator supports three events
    // each of which is open coded in the body of the simulator.

    private long time = new Date().getTime();

    private long nextSearchComplete = 0;
    private long nextPlayStarted = 0;
    private long nextPlayComplete = 0;
    
    public long nextEvent(long bound) {
        long result = bound;
        result = sooner(result, nextSearchComplete);
        result = sooner(result, nextPlayStarted);
        result = sooner(result, nextPlayComplete);
        return result;
    }

    public long sooner (long soon, long event) {
        return event > time && event < soon ? event : soon;
    }

    public void perform() {
        if (time == nextSearchComplete)     {musicPlayer.searchComplete();}
        if (time == nextPlayStarted)        {musicPlayer.playStarted();}
        if (time == nextPlayComplete)       {musicPlayer.playComplete();}
    }

    public void advance (long future) {
        while (time < future) {
            time = nextEvent(future);
            perform();
        }
    }

    public long schedule(double seconds){
        return time + (long)(1000 * seconds);
    }

    public void delay (double seconds) {
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

    public void setNextSearchComplete(long nextSearchComplete) {
        this.nextSearchComplete = nextSearchComplete;
    }

    public void setNextPlayStarted(long nextPlayStarted) {
        this.nextPlayStarted = nextPlayStarted;
    }

    public void setNextPlayComplete(long nextPlayComplete) {
        this.nextPlayComplete = nextPlayComplete;
    }

    public long getNextPlayComplete() {
        return nextPlayComplete;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
