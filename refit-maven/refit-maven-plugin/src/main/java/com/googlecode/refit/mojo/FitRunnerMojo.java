/*
 * Copyright 2011 Harald Wellmann
 * Based on earlier work of Mauro Talevi
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
package com.googlecode.refit.mojo;

import static java.lang.Thread.currentThread;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.googlecode.refit.runner.ReportGenerator;
import com.googlecode.refit.runner.RunnerListener;
import com.googlecode.refit.runner.TreeRunner;
import com.googlecode.refit.runner.jaxb.TestResult;

/**
 * Mojo to run Fit tests via a Fixture
 * 
 * @author Mauro Talevi, Harald Wellmann
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 */
public class FitRunnerMojo extends AbstractMojo implements RunnerListener {

    private static final String COMMA = ",";

    private static final String EXECUTION_PARAMETERS = "sourceDirectory={0}, caseSensitive={1},"
            + " sourceIncludes={2}, sourceExcludes={3}, parseTags={4}, outputDirectory={5}, ignoreFailures={6}";

    /**
     * Classpath.
     * 
     * @parameter expression="${project.testClasspathElements}"
     * @required
     */
    protected List<String> classpathElements;

    /**
     * The source directory containing the Fit fixtures
     * 
     * @parameter
     * @required
     */
    protected String sourceDirectory;

    /**
     * Flag to indicate that path names are case sensitive
     * 
     * @parameter default-value="false"
     */
    protected boolean caseSensitive;

    /**
     * The filter for source file includes, relative to the source directory, as CSV patterns.
     * 
     * @parameter
     */
    protected String sourceIncludes;

    /**
     * The filter for source file excludes, relative to the source directory, as CSV patterns.
     * 
     * @parameter
     */
    protected String sourceExcludes;

    /**
     * The parse tags used to identify the Fit tables.
     * 
     * @parameter
     */
    protected String[] parseTags = new String[] { "table", "tr", "td" };

    /**
     * The output directory where the results of Fit processing is written to
     * 
     * @parameter
     * @required
     */
    protected String outputDirectory;

    /**
     * The option to ignore fixture failures
     * 
     * @parameter default-value="false"
     */
    protected boolean ignoreFailures;
    
    /**
     * Flag for skipping Fit execution.
     * @parameter default-value="false"
     */
    protected boolean skip;

    private ClasspathClassLoader testClassLoader;

    private ReportGenerator reportGenerator;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final String executionParameters = MessageFormat.format(EXECUTION_PARAMETERS, new Object[] {
                sourceDirectory, Boolean.valueOf(caseSensitive), sourceIncludes, sourceExcludes,
                Arrays.asList(parseTags), outputDirectory, Boolean.valueOf(ignoreFailures) });
        if (skip) {
            getLog().info("Skipping Fit tests");
            return;
        }
        getLog().debug("Executing FitRunner with parameters " + executionParameters);
        System.setProperty("fit.inputDir", sourceDirectory);
        
        
        /*
         * The test classpath of the project using this plugin is not visible to the classloader
         * of this plugin, but it may be needed to loaded classes and resources for fixtures.
         * So we build our own class loader for the test classpath and temporarily install it
         * as context class loader.
         * 
         * In addition, we preload any JDBC drivers to avoid class loader issues.
         */
        
        loadJdbcDrivers();
        ClassLoader ccl = currentThread().getContextClassLoader();
        try {
            currentThread().setContextClassLoader(getTestClassLoader());
            run(sourceDirectory, caseSensitive, sourceIncludes, sourceExcludes, outputDirectory);
        }
        catch (Exception e) {
            throw new MojoExecutionException("Failed to execute FitRunner with parameters "
                    + executionParameters, e);
        }
        finally {
            currentThread().setContextClassLoader(ccl);
        }
    }

    protected ClasspathClassLoader getTestClassLoader() {
        if (testClassLoader == null) {
            try {
                testClassLoader = new ClasspathClassLoader(classpathElements, getClass().getClassLoader());
            }
            catch (MalformedURLException exc) {
                throw new IllegalStateException("error in classpath", exc);
            }
            getLog().debug("Created test path classloader with classpathElements " + classpathElements);
        }
        return testClassLoader;
    }

    /**
     * Get all JDBC drivers available from the DriverManager. Without doing this, 
     * DriverManager.getDriver() might fail, if some other Mojo of the current project has already 
     * used a database connection. 
     * <p>
     * This is due to the implementation of DriverManager.getDriver() which checks if the driver is
     * equal to the one obtained from the caller's class loader.
     * <p>
     * For reasons I don't fully understand, just calling DriverManager.getDrivers() here in the
     * context of what will be the caller's class loader is sufficient to avoid this 
     * problem.
     * <p>
     * The problem would not occur if DriverManager would always use the ThreadContextClassLoader
     * instead of the caller's class loader.
     */
	private void loadJdbcDrivers() {
		Enumeration<Driver> e = DriverManager.getDrivers();
		if (getLog().isDebugEnabled()) {
			DriverManager.setLogWriter(new PrintWriter(System.out));
			while (e.hasMoreElements()) {
				Driver driver = e.nextElement();
				getLog().debug(driver + " loaded by " +  driver.getClass().getClassLoader());
			}
			DriverManager.setLogWriter(null);
		}
	}

    protected void run(String sourceDirectory, boolean caseSensitive, String sourceIncludes,
            String sourceExcludes, String outputDirectory) throws Exception {
        File inputDir = new File(sourceDirectory);
        File outputDir = new File(outputDirectory);
        String[] includes = null;
        String[] excludes = null;
        if (sourceIncludes != null) {
            getLog().debug("Setting includes " + sourceIncludes);
            includes = sourceIncludes.split(COMMA);
        }
        if (sourceExcludes != null) {
            getLog().debug("Setting excludes " + sourceExcludes);
            excludes = sourceExcludes.split(COMMA);
        }
        
        reportGenerator = new ReportGenerator(inputDir, outputDir);
        TreeRunner runner = new TreeRunner(inputDir, outputDir, includes, excludes, this);
        runner.run();
    }

    @Override
    public void beforeTest(String testPath) {
        getLog().debug("running Fit test " + testPath);
        reportGenerator.beforeTest(testPath);
    }

    @Override
    public void afterTest(TestResult result) {
        if (!result.isPassed()) {
            String message = String.format("Fit test %s failed with %d right, " +
            		"%d wrong, %d ignored, %d exceptions",
                    result.getPath(), result.getRight(), result.getWrong(), result.getIgnored(), 
                    result.getExceptions());
            
            if (ignoreFailures) {
                getLog().warn(message);
            }
            else {
                throw new IllegalStateException(message);
            }
        }
        reportGenerator.afterTest(result);
    }

    @Override
    public void afterSuite() {
        getLog().debug("writing reports");
        reportGenerator.afterSuite();
    }
}
