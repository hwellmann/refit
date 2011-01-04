package eg;

// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

import fit.*;

public class ArithmeticFixture extends PrimitiveFixture {

	int x=0;
	int y=0;

    public void doRows(Parse rows) {
        super.doRows(rows.more);    // skip column heads
    }

    public void doCell(Parse cell, int column) {
        switch (column) {
            case 0: x = (int)parseLong(cell); break;
            case 1: y = (int)parseLong(cell); break;
            case 2: check(cell, x+y); break;
            case 3: check(cell, x-y); break;
            case 4: check(cell, x*y); break;
            case 5: check(cell, x/y); break;
            default: ignore(cell); break;
        }
    }
}
