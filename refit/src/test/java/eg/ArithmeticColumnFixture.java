package eg;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.ColumnFixture;
import fit.ScientificDouble;

public class ArithmeticColumnFixture extends ColumnFixture {

    public int x;
    public int y;

    public int plus () {
        return x + y;
    }

    public int minus() {
        return x - y;
    }

    public int times () {
        return x * y;
    }

    public int divide () {
        return x / y;
    }

    public float floating () {
        return (float)x / (float)y;
    }

    public ScientificDouble  sin () {
        return new ScientificDouble(Math.sin(Math.toRadians(x)));
    }

    public ScientificDouble  cos () {
        return new ScientificDouble(Math.cos(Math.toRadians(x)));
    }

}
