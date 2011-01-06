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
 * The suite first builds a list of input files to be run as FIT tests. The input files are
 * located under the {@code inputDir} root. The file patterns to be included or excluded are specified
 * by the {@code includes} and {@code excludes} properties. The default include pattern is
 * {@code **&#47;*.html}, the exclude pattern list is empty by default.
 * Each of these properties can be a list of Strings
 * interpreted as Ant file patterns.
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
     * Constructor for internal use by JUnit. Applications may only use this class 
     * as argument to the {@link RunWith} annotation.
     * 
     * @param klass  the class to be run as a test suite
     * @throws InitializationError
     */
    public FitSuite(Class<?> klass) throws InitializationError {
        super(klass, Collections.<Runner>emptyList());
		buildRunners();
    }

    /**
     * Builds a list of Runners, one for each file matching the input patterns.
     * @throws InitializationError
     */
    private void buildRunners() throws InitializationError {
        FitConfiguration fc = getTestClass().getJavaClass().
            getAnnotation(FitConfiguration.class);
        if (fc == null) {
            String msg = "class run with FitSuite must be annotated with @FitConfiguration";
            throw new InitializationError(msg);
        }
		try {
	        DefaultFitConfiguration config = fc.value().newInstance();
	        assert config.getInputDir() != null;
	        assert config.getOutputDir() != null;
	        
			System.setProperty("fit.inputDir", config.getInputDir());
	        File inputDirectory = new File(config.getInputDir());
	        File outputDirectory = new File(config.getOutputDir());
	        
	        DirectoryScanner scanner = new DirectoryScanner();
	        scanner.setBasedir(inputDirectory);
	        scanner.setIncludes(config.getIncludes());
	        scanner.setExcludes(config.getExcludes());
	        scanner.scan();
	        for (String testPath : scanner.getIncludedFiles()) {
	            FitRunner runner = new FitRunner(inputDirectory, outputDirectory, testPath);
	            runners.add(runner);
	        }    

		} catch (InstantiationException exc) {
			throw new InitializationError(exc);
		} catch (IllegalAccessException exc) {
			throw new InitializationError(exc);
		}

    }
    
	/**
     * Returns the list of runners corresponding to FIT test files.
     * @return FIT test runners
     *  
     */
    @Override
    protected List<Runner> getChildren() {
        return runners;
    }
    
}
