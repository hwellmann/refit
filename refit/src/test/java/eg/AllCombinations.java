// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fit.Parse;

public class AllCombinations extends AllFiles {

    protected List<List<File>> lists = new ArrayList<List<File>>();
    protected Parse row;
    protected int caseNumber = 1;

    public void doTable(Parse table) {
        row = table.parts.last();
        super.doTable(table);
        combinations();
    }

    @Override
    protected void doRow (Parse row, List<File> files){
        lists.add(files);
    }

    protected void combinations() {
        combinations(0, new ArrayList<File>(lists.size()));
    }

    protected void combinations(int index, List<File> combination) {
        if (index == lists.size()) {
            doCase(combination);
        } else {
            List<File> files = lists.get(index);
            for (Iterator<File> i=files.iterator(); i.hasNext(); ) {
                combination.set(index, i.next());
                combinations(index+1, combination);
            }
        }
    }

    protected void doCase(List<File> combination) {
        Parse number = tr(td("#"+caseNumber++, null), null);
        number.leaf().addToTag(" colspan=2");
        row.last().more = number;
        super.doRow(number, combination);
    }
}
