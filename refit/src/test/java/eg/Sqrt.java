package eg;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.ColumnFixture;

public class Sqrt extends ColumnFixture {
    public double value;
    public double sqrt() throws Exception {
        if (value < 0) throw new Exception("netagive sqrt");
        return Math.sqrt(value);
    }
}

