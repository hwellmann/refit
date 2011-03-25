// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Read license.txt in this directory.

package eg;

import fit.*;
import java.io.*;

public class ExampleTests extends ColumnFixture {

    public String file;
    public boolean wiki;

    protected String input;
    protected Parse tables;
    protected Fixture fixture;
    protected Counts runCounts = new Counts();
    protected String footnote = null;

    protected void run() throws Exception {
        input = read(new File("Documents/"+file));
        fixture = new Fixture();
        if (wiki) {
            tables = new Parse(input, new String[]{"wiki", "table", "tr", "td"});
            fixture.doTables(tables.parts);
        } else {
            tables = new Parse(input, new String[]{"table", "tr", "td"});
            fixture.doTables(tables);
        }
        runCounts.tally(fixture.counts);
        summary.put("counts run", runCounts);
    }


    public int right() throws Exception {
        run();
        return fixture.counts.right;
    }

    public int wrong() {
        return fixture.counts.wrong;
    }

    public int ignores() {
        return fixture.counts.ignores;
    }

    public int exceptions() {
        return fixture.counts.exceptions;
    }

    protected String read(File input) throws IOException {
        char chars[] = new char[(int)(input.length())];
        FileReader in = new FileReader(input);
        in.read(chars);
        in.close();
        return new String(chars);
    }


    // Footnote /////////////////////////////////

    Parse fileCell;

    public void doRow(Parse row) {
        fileCell = row.leaf();
        super.doRow(row);
    }

    @SuppressWarnings("deprecation")
    public void wrong(Parse cell) {
        super.wrong(cell);
        if (footnote==null) {
            footnote = tables.footnote();
            fileCell.addToBody(footnote);
        }
    }

}
