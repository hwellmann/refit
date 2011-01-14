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
package com.googlecode.refit.junit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * Use this class to run a suite of FIT tests under JUnit. To do so, create a class with the
 * following annotations:
 * 
 * <pre>
 * &#64;RunWith(FitSuite.class)
 * &#64;FitConfiguration(MyFitSuite.Configuration.class)
 * public class MyFitSuite {
 *     
 *     public static Configuration extends DefaultFitConfiguration {
 *     
 *         @Overrride
 *         public String getInputDir() {
 *            return System.getProperty("myFitRoot");
 *         }
 *     
 *         @Overrride
 *         public String getOutputDir() {
 *            return "build/html";
 *         }
 *     }
 * }
 * </pre>
 * 
 * The suite first builds a list of input files to be run as FIT tests. The input files are located
 * under the {@code inputDir} root. The file patterns to be included or excluded are specified by
 * the {@code includes} and {@code excludes} properties. The default include pattern is
 * {@code **&#47;*.html}, the exclude pattern list is empty by default. Each of these properties can
 * be a list of Strings interpreted as Ant file patterns.
 * <p>
 * The suite then runs all files matching at least one of the include patterns and none of the
 * exclude patterns.
 * <p>
 * Each test file will be a direct child of the suite, named by its relative path.
 * 
 * @author hwellmann
 * 
 */
public class FitSuite extends Suite {

    private List<Runner> runners = new ArrayList<Runner>();

    /**
     * Constructor for internal use by JUnit. Applications may only use this class as argument to
     * the {@link RunWith} annotation.
     * 
     * @param klass
     *            the class to be run as a test suite
     * @throws InitializationError
     */
    public FitSuite(Class<?> klass) throws InitializationError {
        super(klass, Collections.<Runner> emptyList());
        buildRunners();
    }

    /**
     * Builds a list of Runners, one for each file matching the input patterns.
     * 
     * @throws InitializationError
     */
    private void buildRunners() throws InitializationError {
        FitConfiguration fc = getTestClass().getJavaClass().getAnnotation(FitConfiguration.class);
        if (fc == null) {
            String msg = "class run with FitSuite must be annotated with @FitConfiguration";
            throw new InitializationError(msg);
        }
        try {
            DefaultFitConfiguration config = fc.value().newInstance();
            assert config.getInputDir() != null;
            assert config.getOutputDir() != null;
            assert config.getIncludes() != null;
            assert config.getExcludes() != null;

            System.setProperty("fit.inputDir", config.getInputDir());
            File inputDirectory = new File(config.getInputDir());
            File outputDirectory = new File(config.getOutputDir());

            DirectoryScanner scanner = new DirectoryScanner();
            scanner.setBasedir(inputDirectory);
            scanner.setIncludes(config.getIncludes());
            scanner.setExcludes(config.getExcludes());
            scanner.scan();
            String[] files = scanner.getIncludedFiles();
            assert files != null;
            if (files.length == 0) {
                throw new InitializationError("no matching input files");
            }
            for (String testPath : files) {
                FitRunner runner = new FitRunner(inputDirectory, outputDirectory, testPath);
                runners.add(runner);
            }

        }
        catch (InstantiationException exc) {
            throw new InitializationError(exc);
        }
        catch (IllegalAccessException exc) {
            throw new InitializationError(exc);
        }

    }

    /**
     * Returns the list of runners corresponding to FIT test files.
     * 
     * @return FIT test runners
     * 
     */
    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

}
