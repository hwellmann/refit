// Copyright (c) 2003 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package eg;

import fit.*;

import java.util.*;

public class BinaryChop extends ColumnFixture {

// Test Fixture /////////////////////////////////

    public int key, array[];

    public void execute() {
        int empty[] = {};
        if (array==null) array=empty;
    }

    public int result() {
        return chopFriday(key, array) ;
    }

    public int mon() {return chopMonday(key, array);}
    public int tue() {return chopTuesday(key, array);}
    public int wed() {return chopWednesday(key, array);}
    public int thr() {return chopThursday(key, array);}
    public int fri() {return chopFriday(key, array);}

// Search Methods ///////////////////////////////

    int chopMonday (int key, int array[]) {
        int min = 0;
        int max = array.length-1;
        while (min<=max) {
            int probe = (min+max)/2;
            if (key == array[probe]) {
                return probe;
            } else if (key > array[probe]) {
                min = probe+1;
            } else {
                max = probe-1;
            }
        }
        return -1;
    }

    int chopTuesday (int key, int array[]) {
        int min = 0;
        int max = array.length-1;
        while (min<=max) {
            int probe = (min+max)/2;
            switch (new Integer(key).compareTo(new Integer(array[probe]))) {
            case (0):
                return probe;
            case (1):
                min = probe+1;
                break;
            case (-1):
                max = probe-1;
                break;
            default:
                throw new Error("unexpected result from compareTo");
            }
        }
        return -1;
    }

    int chopWednesday (int key, int array[]) {
        if (array.length==0) return -1;
        int probe = array.length/2;
        if (key==array[probe]) return probe;
        if (key<array[probe]) return chopWednesday(key, subarray(array, 0, probe));
        int result = chopWednesday(key, subarray(array, probe+1, array.length-(probe+1)));
        return result < 0 ? result : result + probe+1;
    }

    int chopThursday (int key, int array[]) {
        int min = 0;
        int max = array.length-1;
        Random gen = new Random();

        while (min<=max) {
            int probe = (int)(gen.nextDouble()*(max-min))+min;
            if (key == array[probe]) {
                return probe;
            } else if (key > array[probe]) {
                min = probe+1;
            } else {
                max = probe-1;
            }
        }
        return -1;
    }

    int chopFriday (int key, int array[]) {
        for (int i=0; i<array.length; i++) {
            if (key==array[i]) return i;
        }
        return -1;
    }


// Utilities ////////////////////////////////////

    int[] subarray (int[] source, int pos, int length) {
        int dest[] = new int [length];
        for (int i=0; i<length; i++)
            dest[i]=source[pos+i];
        return dest;
    }

}
