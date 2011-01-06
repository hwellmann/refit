package eg.net;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

// import java.util.regex.*;

import java.util.StringTokenizer;
import java.text.DecimalFormat;

public class GeoCoordinate {

    float lat;
    float lon;

    public GeoCoordinate (float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static GeoCoordinate parse (String string) {
        StringTokenizer t = new StringTokenizer (string, "nNsSeEwW \'\",", true);
        float n[] = {0,0,0,0,0,0};
        boolean north=true, east=true;
        for (int i=0; i<6 && t.hasMoreTokens(); ) {
            String token = t.nextToken().toLowerCase();
            char ch = token.charAt(0);
            if (Character.isDigit(ch) || ch == '-')     {n[i++] = Float.parseFloat(token);}
            if (ch == 's')                              {north = false;}
            if (ch == 'w')                              {east = false;}
            if (i>0 && "nsew".indexOf(ch)>=0)           {i = 3;}
        }
        float lat = n[0] + n[1]/60 + n[2]/3600;
        float lon = n[3] + n[4]/60 + n[5]/3600;
        return new GeoCoordinate(north?lat:-lat, east?lon:-lon);
    }

    static double precision = 0.00001;

    public boolean equals(Object arg) {
        if (!getClass().isInstance(arg)) return false;
        GeoCoordinate coord = (GeoCoordinate)arg;
        return
            ((long)(lat/precision)) == ((long)(coord.lat/precision)) &&
            ((long)(lon/precision)) == ((long)(coord.lon/precision));
    }

    public long hash() {
        return ((long)(lat/precision)) + ((long)(lon/precision));
    }

    public String toString () {
        DecimalFormat coord = new DecimalFormat();
        coord.setMaximumFractionDigits(4);
        return
            coord.format(Math.abs(lat)) + (lat>=0 ? "N " : "S ") +
            coord.format(Math.abs(lon)) + (lon>=0 ? "E " : "W ");
    }
}
