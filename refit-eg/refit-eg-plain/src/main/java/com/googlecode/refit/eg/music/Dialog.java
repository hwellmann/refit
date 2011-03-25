// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package com.googlecode.refit.eg.music;

import fit.*;

public class Dialog extends Fixture {
    String message;
    Fixture caller;

    Dialog (String message, Fixture caller) {
        this.message = message;
        this.caller = caller;
    }

    public String message() {
        return message;
    }

    public void ok () {
        if (message.equals("load jamed"))   {MusicPlayer.stop();}
        ActionFixture.actor = caller;
    }

}
