// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.osgi.eg.music;

import java.util.Date;

public class Music {

    private int id;
    
    public String title;
    public String artist;
    public String album;
    public String genre;
    public long size;
    public int seconds;
    public int trackNumber;
    public int trackCount;    
    public int year;    
    public Date date;
    
    public transient boolean selected;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String track() {
        return trackNumber + " of " + trackCount;
    }

    public double time() {
        return Math.round(seconds / 0.6) / 100.0;
    }

    public String toString() {
        if (title != null) {
            return title;
        } else {
            return super.toString();
        }
    }
}
