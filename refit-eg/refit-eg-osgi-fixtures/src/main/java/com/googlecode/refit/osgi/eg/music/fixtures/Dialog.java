// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.osgi.eg.music.fixtures;

import com.googlecode.refit.osgi.eg.music.EventListener;
import com.googlecode.refit.osgi.eg.music.MusicPlayer;

import fit.ActionFixture;
import fit.Fixture;

public class Dialog extends Fixture implements EventListener {
    
    private MusicPlayer musicPlayer;
    
    String message;
    Fixture caller;

    protected void setMusicPlayer(MusicPlayer player) {
        this.musicPlayer = player;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Fixture getCaller() {
        return caller;
    }

    public void setCaller(Fixture caller) {
        this.caller = caller;
    }

    public String message() {
        return message;
    }

    public void ok () {
        if (message.equals("load jamed"))   {musicPlayer.stop();}
        ActionFixture.actor = caller;
    }

    @Override
    public void searchComplete() {
        // nothing
    }

    @Override
    public void playStarted() {
        // nothing
    }

    @Override
    public void playComplete() {
        // nothing
    }

    @Override
    public void loadJammed() {
        setMessage("load jamed");
        setCaller(ActionFixture.actor);
        ActionFixture.actor = this;
    }

}
