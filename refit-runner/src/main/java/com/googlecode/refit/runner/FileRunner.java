/*
 * Copyright 2011 Harald Wellmann
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
package com.googlecode.refit.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

import com.googlecode.refit.runner.jaxb.TestResult;

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
     * the input directory and writes the processed to file to the same path relative to the
     * output directory.
     */
    private String testPath;
    
    /** Default fixture for running the test. */
    private Fixture fixture;
    
    private TestResult result;

    private RunnerListener listener;
    
    public FileRunner(File inputDir, File outputDir, String testPath) {
        this(inputDir, outputDir, testPath, new DefaultRunnerListener());
    }

    public FileRunner(File inputDir, File outputDir, String testPath, RunnerListener listener) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.testPath = testPath;
        this.listener = listener;
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
            throw new FitException(exc);
        }
        catch (ParseException exc) {
            throw new FitException(exc);
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
        listener.beforeTest(testPath);
        String input = read(reader);
        Parse tables = new Parse(input);
        fixture = new Fixture();
        fixture.doTables(tables);
        PrintWriter output = new PrintWriter(writer);
        tables.print(output);
        output.flush();
        boolean passed = passed(fixture);
        result = new TestResult();
        result.setPath(testPath);
        result.setPassed(passed);
        result.setRight(fixture.getCounts().right);
        result.setWrong(fixture.getCounts().wrong);
        result.setIgnored(fixture.getCounts().ignores);
        result.setExceptions(fixture.getCounts().exceptions);
        listener.afterTest(result);
        
        return passed;
    }

    private boolean passed(Fixture fixture) {
        if (fixture.getCounts().wrong > 0 || fixture.getCounts().exceptions > 0) {
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
    public static String read(Reader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        StringBuffer sb = new StringBuffer();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            sb.append('\n');
            line = br.readLine();
        }
        in.close();
        return sb.toString();
    }
    
    public TestResult getResult() {
        return result;
    }
}
