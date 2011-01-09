// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package com.googlecode.refit.glassfish.eg.music;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Music {

    @Id
    private int id;
    
    public String title;
    public String artist;
    public String album;
    public String genre;
    public long size;
    public int seconds;
    public int trackNumber;
    public int trackCount;
    
    // YEAR is a keyword in SQL-92 and cannot be used as column name with Derby
    @Column(name = "year_")
    public int year;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date date;
    

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
