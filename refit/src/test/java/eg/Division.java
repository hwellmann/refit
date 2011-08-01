package eg;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.ColumnFixture;

public class Division extends ColumnFixture {
    public float numerator;
    public float denominator;
    public float quotient() {
        return numerator / denominator;
    }
}
