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
