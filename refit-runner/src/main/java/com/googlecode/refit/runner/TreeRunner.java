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

import java.io.File;

import org.apache.tools.ant.DirectoryScanner;

import com.googlecode.refit.runner.jaxb.TestResult;

public class TreeRunner {
    
    public static final String DEFAULT_INCLUDE = "**/*.html";
    
    private File inputDir;
    private File outputDir;
    private String[] includes;
    private String[] excludes;
    private RunnerListener listener;
    
    private boolean success;

    public TreeRunner(File inputDir, File outputDir) {
        this(inputDir, outputDir, DEFAULT_INCLUDE);
    }

    public TreeRunner(File inputDir, File outputDir, String[] includes) {
        this(inputDir, outputDir, includes, (String[])null);
    }

    public TreeRunner(File inputDir, File outputDir, String include) {
        this(inputDir, outputDir, new String[] { include }, (String[])null);
    }

    public TreeRunner(File inputDir, File outputDir, RunnerListener listener) {
        this(inputDir, outputDir, DEFAULT_INCLUDE, listener);
    }

    public TreeRunner(File inputDir, File outputDir, String[] includes, RunnerListener listener) {
        this(inputDir, outputDir, includes, null, listener);
    }

    public TreeRunner(File inputDir, File outputDir, String include, RunnerListener listener) {
        this(inputDir, outputDir, new String[] { include }, null, listener);
    }

    public TreeRunner(File inputDir, File outputDir, String[] includes, String[] excludes) {
        this(inputDir, outputDir, includes, excludes, new DefaultRunnerListener());
    }
    
    public TreeRunner(File inputDir, File outputDir, String[] includes, String[] excludes,
            RunnerListener listener) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.includes = includes;
        this.excludes = excludes;
        this.listener = listener;
    }

    public boolean run() {
        assert inputDir != null;
        assert outputDir != null;
        assert includes != null;

        System.setProperty("fit.inputDir", inputDir.getPath());

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(inputDir);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        assert files != null;
        if (files.length == 0) {
            throw new FitException("no matching input files");
        }
        for (String testPath : files) {
            runSingleTest(testPath);
        }
        return success;
    }

    private void runSingleTest(String testPath) {
        listener.beforeTest(testPath);
        FileRunner runner = new FileRunner(inputDir, outputDir, testPath);
        boolean passed = runner.run();
        success &= passed;
        
        TestResult result = runner.getResult();
        listener.afterTest(result);
    }
}
