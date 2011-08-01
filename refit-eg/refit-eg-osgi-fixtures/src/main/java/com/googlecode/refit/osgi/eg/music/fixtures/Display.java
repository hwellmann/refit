// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.osgi.eg.music.fixtures;

import java.util.Date;

import com.googlecode.refit.osgi.eg.music.Music;
import com.googlecode.refit.osgi.eg.music.MusicLibrary;

public class Display extends fit.RowFixture {
    
    private MusicLibrary musicLibrary;

    protected void setMusicLibrary(MusicLibrary library) {
        this.musicLibrary = library;
    }
    
    public Class<?> getTargetClass() {
        return Music.class;
    }

    public Object[] query() {
        return musicLibrary.displayContents();
    }

    public Object parse (String s, Class<?> type) throws Exception {
        if (type.equals(Date.class))    {return musicLibrary.parseDate(s);}
        return super.parse (s, type);
    }
}
