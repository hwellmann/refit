/*
 * Copyright 2011 Harald Wellmann
 * Copyright (c) 2002, 2008 Cunningham & Cunningham, Inc.
 *
 * This file is part of reFit.
 * 
 * reFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * reFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with reFit.  If not, see <http://www.gnu.org/licenses/>.
 */
package fit;

// Warning: not (yet) a general number usable in all calculations.

public class ScientificDouble extends Number implements Comparable<Object> {

    private static final long serialVersionUID = 1L;
    
    protected double value;
    protected double precision;

    public ScientificDouble(double value) {
        this.value = value;
        this.precision = 0;
    }

    public static ScientificDouble valueOf(String s) {
        ScientificDouble result = new ScientificDouble(Double.parseDouble(s));
        result.precision = precision(s);
        return result;
    }

    public static double precision (String s) {
        double value = Double.parseDouble(s);
        double bound = Double.parseDouble(tweak(s.trim()));
        return Math.abs(bound-value);
    }

    public static String tweak(String s) {
        int pos;
        if ((pos = s.toLowerCase().indexOf("e"))>=0) {
            return tweak(s.substring(0,pos)) + s.substring(pos);
        }
        if (s.indexOf(".")>=0) {
            return s + "5";
        }
        return s+".5";
    }



    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }

    public int compareTo(Object obj) {
        double other = ((Number)obj).doubleValue();
        double diff = value-other;
        // System.out.println(value+" "+precision+" "+diff);
        if (diff < -precision) return -1;
        if (diff > precision) return 1;
        if (Double.isNaN(value) && Double.isNaN(other)) return 0;
        if (Double.isNaN(value)) return 1;
        if (Double.isNaN(other)) return -1;
        return 0;
    }

    public String toString() {
        return Double.toString(value);
    }

    public double doubleValue() {
        return value;
    }

    public float floatValue() {
        return (float)value;
    }

    public long longValue() {
        return (long)value;
    }

    public int intValue() {
        return (int)value;
    }
}
