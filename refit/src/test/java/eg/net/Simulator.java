package eg.net;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.Fixture;

public class Simulator extends Fixture {
    int zip[];
    GeoCoordinate coord;
    int nodes=0;

    public void newCity() {
    }

    public void ok() {
        nodes++;
    }

    public void cancel() {
    }

    public void name(String n) {
    }

    public void zip(int[] z) {
        zip = z;
    }

    public int[] zip() {
        return zip;
    }

    public void population (Float p) {
    }

    public void coord (GeoCoordinate c) {
        coord = c;
    }

    public GeoCoordinate coord () {
        return coord;
    }


    public int nodes() {
        return nodes;
    }

    public Object parse (String string, Class<?> type) throws Exception {
        if (type.equals(GeoCoordinate.class)) {return GeoCoordinate.parse(string);}
        return super.parse (string, type);
    }

}
