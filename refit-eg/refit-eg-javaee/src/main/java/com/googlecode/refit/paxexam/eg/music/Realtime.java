// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.paxexam.eg.music;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import fit.Parse;
import fit.TimedActionFixture;

@ApplicationScoped
public class Realtime extends TimedActionFixture {

    @Inject
    private Simulator system;
    
    public Date time () {
        return new Date(system.getTime());
    }

    public void pause () {
        double seconds = Double.parseDouble(cells.more.text());
        system.delay(seconds);
    }

    public void await () throws Exception {
        system("wait", cells.more);
    }

    public void fail () throws Exception {
        system("fail", cells.more);
    }

    public void enter() throws Exception {
        system.delay(0.8);
        super.enter();
    }

    public void press() throws Exception {
        system.delay(1.2);
        super.press();
    }

    private void system(String prefix, Parse cell) throws Exception {
        String method = camel(prefix+" "+cell.text());
        try {
            system.getClass().getMethod(method).invoke(system);
        } catch (Exception e) {
            exception (cell, e);
        }
    }
}
