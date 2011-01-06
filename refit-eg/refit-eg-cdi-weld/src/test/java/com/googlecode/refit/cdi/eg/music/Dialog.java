// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.cdi.eg.music;

import javax.inject.Inject;

import fit.ActionFixture;
import fit.Fixture;

public class Dialog extends Fixture {
    
    @Inject
    private MusicPlayer musicPlayer;
    
    String message;
    Fixture caller;

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

}
