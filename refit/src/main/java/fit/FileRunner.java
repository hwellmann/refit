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

import java.io.*;
import java.util.*;

public class FileRunner {

    private static String encoding;

    public String input;
    public Parse tables;
    public Fixture fixture = new Fixture();
    public PrintWriter output;

    public static void main(String argv[]) {
        try {
            new FileRunner().run(argv);
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("fit: " + e.getMessage() + ": unsupported encoding");
            System.exit(-1);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void run(String argv[]) throws IOException, CommandLineException {
        Parameters parameters = new Parameters(argv);
        if (encoding == null || parameters.encodingSpecified()) {
            encoding = parameters.encoding();
        }

        args(parameters.legacyArguments());
        process();
        exit();
    }

    public void process() {
        try {
            if (input.indexOf("<wiki>") >= 0) {
                tables = new Parse(input, new String[] { "wiki", "table", "tr", "td" });
                fixture.doTables(tables.parts);
            }
            else {
                tables = new Parse(input, new String[] { "table", "tr", "td" });
                fixture.doTables(tables);
            }
        }
        catch (Exception e) {
            exception(e);
        }
        tables.print(output);
    }

    public void args(String[] argv) throws IOException {
        if (argv.length != 2) {
            System.err.println("usage: java fit.FileRunner input-file output-file");
            System.exit(-1);
        }
        File in = new File(argv[0]);
        File out = new File(argv[1]);
        fixture.getSummary().put("input file", in.getAbsolutePath());
        fixture.getSummary().put("input update", new Date(in.lastModified()));
        fixture.getSummary().put("output file", out.getAbsolutePath());
        input = read(in);
        output = new PrintWriter(out, encoding);
    }

    protected String read(File input) throws IOException {
        char chars[] = new char[(int) (input.length())];
        Reader in = new InputStreamReader(new FileInputStream(input), encoding);
        in.read(chars);
        in.close();
        return new String(chars);
    }

    protected void exception(Exception e) {
        tables = new Parse("body", "Unable to parse input. Input ignored.", null, null);
        fixture.exception(tables, e);
    }

    protected void exit() {
        output.close();
        System.err.println(fixture.counts());
        System.exit(fixture.getCounts().wrong + fixture.getCounts().exceptions);
    }

}
