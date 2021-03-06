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
package com.googlecode.refit.osgi;

import java.io.File;

import org.apache.tools.ant.DirectoryScanner;

public class TreeRunner {
    
    public static final String DEFAULT_INCLUDE = "**/*.html";

    private File inputDir;
    private File outputDir;
    private String[] includes;
    private String[] excludes;

    public TreeRunner(File inputDir, File outputDir) {
        this(inputDir, outputDir, DEFAULT_INCLUDE);
    }

    public TreeRunner(File inputDir, File outputDir, String[] includes) {
        this(inputDir, outputDir, includes, null);
    }

    public TreeRunner(File inputDir, File outputDir, String include) {
        this(inputDir, outputDir, new String[] { include }, null);
    }

    public TreeRunner(File inputDir, File outputDir, String[] includes, String[] excludes) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
        this.includes = includes;
        this.excludes = excludes;
    }

    /**
     * Builds a list of Runners, one for each file matching the input patterns.
     * 
     * @throws InitializationError
     */
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
            throw new RuntimeException("no matching input files");
        }
        boolean success = true;
        for (String testPath : files) {
            FileRunner runner = new FileRunner(inputDir, outputDir, testPath);
            boolean passed = runner.run();
            success &= passed;
        }
        return success;
    }
}
