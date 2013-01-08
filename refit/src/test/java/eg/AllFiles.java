// Copyright (c) 2002 Cunningham & Cunningham, Inc.
// Released under the terms of the GNU General Public License version 2 or later.

package eg;

import fit.*;
import java.io.*;
import java.util.*;

public class AllFiles extends Fixture {

    public void doRow(Parse row) {
        Parse cell = row.leaf();
        List<File> files = expand(cell.text());
        if (files.size()>0) {
            doRow(row, files);
        } else {
            ignore(cell);
            info(cell, " no match");
        }
    }

    protected List<File> expand(String pattern) {
        StringTokenizer tokens = new StringTokenizer(pattern, File.separator);
        List<File> files = new ArrayList<File>();
        expand(new File("."), tokens, files);
        return files;
    }

    protected void expand(File path, StringTokenizer tokens, List<File> result) {
        if (tokens.hasMoreTokens()) {
            File files[] = path.listFiles(new WildCard(tokens.nextToken()));
            for (int i=0; i<files.length; i++) {
                expand(files[i], tokens, result);
            }
        } else {
            result.add(path);
        }
    }

    protected void doRow(Parse row, List<File> files) {
        doFiles(row, files);
    }

    protected void doFiles(Parse row, List<File> files) {
        for (Iterator<File> i=files.iterator(); i.hasNext(); ) {
            File path = i.next();
            Parse cells = td(path.getName(), td("", null));
            row = (row.more = tr(cells, row.more));
            Fixture fixture = new Fixture();
            run(path, fixture, cells);
            summarize(fixture, path);
        }
    }

    public static int runCount=0;

    @SuppressWarnings("deprecation")
    protected void run(File path, Fixture fixture, Parse cells) {
        if (pushAndCheck(path)) {
            ignore(cells);
            info(cells, "recursive");
            return;
        }
        try {
            String input = read(path);
            Parse tables;
            if (input.indexOf("<wiki>") >= 0) {
                tables = new Parse(input, new String[]{"wiki", "table", "tr", "td"});
                fixture.doTables(tables.parts);
            } else {
                tables = new Parse(input, new String[]{"table", "tr", "td"});
                fixture.doTables(tables);
            }

            info(cells.more, fixture.getCounts().toString());
            if (fixture.getCounts().wrong == 0 && fixture.getCounts().exceptions == 0) {
                right(cells.more);
            } else {
                wrong(cells.more);
                cells.more.addToBody(tables.footnote());
            }
        } catch (Exception e) {
            exception (cells, e);
        }
        pop(path);
    }

    public static List<String> fileStack = new ArrayList<String>();

    protected boolean pushAndCheck(File path) {
        String name = path.getAbsolutePath();
        if (fileStack.contains(name)) {
            return true;
        }
        fileStack.add(name);
        return false;
    }

    protected void pop(File path) {
        fileStack.remove(path.getAbsolutePath());
    }

    private void summarize(Fixture fixture, File path) {
        fixture.getSummary().put("input file", path.getAbsolutePath());
        fixture.getSummary().put("input update", new Date(path.lastModified()));
        Counts runCounts = getSummary().containsKey("counts run")
                ? (Counts)getSummary().get("counts run")
                : new Counts();
        runCounts.tally(fixture.getCounts());
        getSummary().put("counts run", runCounts);
    }

    protected String read(File input) throws IOException {
        char chars[] = new char[(int)(input.length())];
        FileReader in = new FileReader(input);
        in.read(chars);
        in.close();
        return new String(chars);
    }


    Parse tr (Parse cells, Parse more) {
        return new Parse("tr", null, cells, more);
    }

    Parse td (String text, Parse more) {
        return new Parse("td", info(text), null, more);
    }

    class WildCard implements FilenameFilter {

        String prefix;
        String sufix;
        int minimum;

        WildCard (String pattern) {
            int star = pattern.indexOf("*");
            if (star >= 0) {
                prefix = pattern.substring(0, star);
                sufix = pattern.substring(star+1);
                minimum = prefix.length() + sufix.length();
            } else {
                prefix = pattern;
                sufix = null;
                minimum = prefix.length();
            }
        }

        public boolean accept(File dir, String name) {
            return
                !(name.startsWith(".")) &&
                name.length() >= minimum &&
                name.startsWith(prefix) &&
                (sufix==null
                    ? name.length() == minimum
                    : name.endsWith(sufix));
        }
    }

    // Self Test ////////////////////////////////

    public static class Expand extends ColumnFixture {

        public String path;
        AllFiles fixture = new AllFiles();

        public String[] expansion() {
            List<File> files = fixture.expand(path);
            String[] result = new String[files.size()];
            for (int i=0; i<result.length; i++) {
                result[i] = ((File)files.get(i)).getName();
            }
            return result;
        }
    }


}

