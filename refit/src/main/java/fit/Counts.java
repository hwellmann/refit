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

public class Counts {
    public int right = 0;
    public int wrong = 0;
    public int ignores = 0;
    public int exceptions = 0;

    public String toString() {
        return
            right + " right, " +
            wrong + " wrong, " +
            ignores + " ignored, " +
            exceptions + " exceptions";
    }

    public void tally(Counts source) {
        right += source.right;
        wrong += source.wrong;
        ignores += source.ignores;
        exceptions += source.exceptions;
    }
}
