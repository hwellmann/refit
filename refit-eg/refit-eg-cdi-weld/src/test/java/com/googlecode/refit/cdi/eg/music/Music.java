// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.cdi.eg.music;

import java.util.*;
import java.text.*;


public class Music {

    static String status = "ready";

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
    public transient boolean selected = false;


    // Accessors ////////////////////////////////

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


    // Factory //////////////////////////////////

    static DateFormat dateFormat = new SimpleDateFormat("M/d/yy h:mm a");

    static Music parse(String string) throws ParseException {
        Music m = new Music();
        StringTokenizer t = new StringTokenizer(string,"\t");
        m.title =       t.nextToken();
        m.artist =      t.nextToken();
        m.album =       t.nextToken();
        m.genre =       t.nextToken();
        m.size =        Long.parseLong(t.nextToken());
        m.seconds =     Integer.parseInt(t.nextToken());
        m.trackNumber = Integer.parseInt(t.nextToken());
        m.trackCount =  Integer.parseInt(t.nextToken());
        m.year =        Integer.parseInt(t.nextToken());
        m.date =        dateFormat.parse(t.nextToken());
        return m;
    }


}
