// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import fit.*;
import java.util.*;

public class AllCombinations extends AllFiles {

    protected List lists = new ArrayList();
    protected Parse row;
    protected int caseNumber = 1;

    public void doTable(Parse table) {
        row = table.parts.last();
        super.doTable(table);
        combinations();
    }

    protected void doRow (Parse row, List files){
        lists.add(files);
    }

    protected void combinations() {
        combinations(0, new ArrayList(lists));
    }

    protected void combinations(int index, List combination) {
        if (index == lists.size()) {
            doCase(combination);
        } else {
            List files = (List)lists.get(index);
            for (Iterator i=files.iterator(); i.hasNext(); ) {
                combination.set(index, i.next());
                combinations(index+1, combination);
            }
        }
    }

    protected void doCase(List combination) {
        Parse number = tr(td("#"+caseNumber++, null), null);
        number.leaf().addToTag(" colspan=2");
        row.last().more = number;
        super.doRow(number, combination);
    }
}
