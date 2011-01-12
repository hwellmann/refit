/*
 * Copyright 2011 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.googlecode.refit.osgi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

import fit.Fixture;
import fit.Parse;

/**
 * Runs a single Fit test.
 * 
 * @see TreeRunner
 * @author hwellmann
 *
 */
public class FileRunner {
   
    /** Input directory for Fit tests. */
    private File inputDir;
    
    /** Output directory for Fit tests. */
    private File outputDir;
    
    /** 
     * Relative path of fit test to be run. The runner runs a file with this path relative to
     * the input directory and write the processed to file to the same path relative to the
     * output directory.
     */
    private String testPath;
    
    /** Default fixture for running the test. */
    private Fixture fixture;
    
    public FileRunner(File inputDir, File outputDir, String testPath) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.testPath = testPath;
    }

    public boolean run() {
        File inputFile = new File(inputDir, testPath);
        File outputFile = new File(outputDir, testPath);
        System.setProperty("fit.currentTest", testPath);
        try {
            ensureParentDirExists(outputFile);
            return run(inputFile, outputFile);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        catch (ParseException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void ensureParentDirExists(File outputFile) throws IOException {
        File parentDir = outputFile.getParentFile();
        if (parentDir.exists()) {
            if (!parentDir.isDirectory()) {
                throw new IOException(parentDir + " is not a directory");
            }
        }
        else if (!parentDir.mkdirs()) {
            throw new IOException("cannot create " + parentDir);
        }
    }
    
    private boolean run(File in, File out) throws IOException, ParseException {
        return run(new FileReader(in), new FileWriter(out));
    }

    private boolean run(Reader reader, Writer writer) throws IOException, ParseException {
        String input = read(reader);
        Parse tables = new Parse(input);
        fixture = new Fixture();
        fixture.doTables(tables);
        PrintWriter output = new PrintWriter(writer);
        tables.print(output);
        output.flush();
        return passed(fixture);
    }

    private boolean passed(Fixture fixture) {
        if (fixture.counts.wrong > 0 || fixture.counts.exceptions > 0) {
            return false;
        }
        return true;
    }

    /**
     * Returns the whole stream as a String.
     * @param in
     * @return
     * @throws IOException
     */
    private String read(Reader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        StringBuffer sb = new StringBuffer();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        in.close();
        return sb.toString();
    }
}
