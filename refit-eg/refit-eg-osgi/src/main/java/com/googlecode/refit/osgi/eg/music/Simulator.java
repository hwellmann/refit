// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.osgi.eg.music;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Simulator {
    
    private ConcurrentLinkedQueue<EventListener> listeners = new ConcurrentLinkedQueue<EventListener>();
    

    // This discrete event simulator supports three events
    // each of which is open coded in the body of the simulator.

    private long time = new Date().getTime();

    public long nextSearchComplete = 0;
    public long nextPlayStarted = 0;
    public long nextPlayComplete = 0;
    
    protected void addEventListener(EventListener listener) {
        listeners.add(listener);
    }
    
    protected void removeEventListener(EventListener listener) {
        listeners.remove(listener);
    }
    
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
        if (time == nextSearchComplete)     {fireSearchComplete();}
        if (time == nextPlayStarted)        {firePlayStarted();}
        if (time == nextPlayComplete)       {firePlayComplete();}
    }

    void advance (long future) {
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
        fireLoadJammed();
    }
    
    private void fireSearchComplete() {
        for (EventListener listener : listeners) {
            listener.searchComplete();
        }
    }
    
    private void firePlayStarted() {
        for (EventListener listener : listeners) {
            listener.playStarted();
        }
    }
    
    private void firePlayComplete() {
        for (EventListener listener : listeners) {
            listener.playComplete();
        }
    }
    
    private void fireLoadJammed() {
        for (EventListener listener : listeners) {
            listener.loadJammed();
        }
    }
    
    public long getTime() {
        return time;
    }
}
