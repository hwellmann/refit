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
package com.googlecode.refit.osgi.test;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.repository;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.vmOption;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Inject;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.googlecode.refit.osgi.FileRunner;
import com.googlecode.refit.osgi.TreeRunner;

import fit.CommandLineException;
import fit.FixtureLoader;

@RunWith(JUnit4TestRunner.class)
public class MusicOsgiTest
{
    public static final String EQUINOX_HOME = "http://download.eclipse.org/equinox/drops/R-3.6.1-201009090800/";
    public static final long DEFAULT_TIMEOUT = 10000;
    public static final String REFIT_VERSION = "1.6.0.SNAPSHOT";

    @Inject
    private BundleContext bundleContext;
   
    @Configuration
    public static Option[] configuration() {

        Option[] options = options(
                
        repository("http://repository.springsource.com/maven/bundles/external"),        
              
        // Bundles under test                
        mavenBundle("com.googlecode.refit", "refit", REFIT_VERSION),
        mavenBundle("com.googlecode.refit", "refit-osgi", REFIT_VERSION),
        mavenBundle("com.googlecode.refit.eg", "refit-eg-osgi-fixtures", REFIT_VERSION),
        mavenBundle("com.googlecode.refit.eg", "refit-eg-osgi", REFIT_VERSION),

        // Declarative Services (Equinox)
        bundle(EQUINOX_HOME + "org.eclipse.equinox.ds_1.2.1.R36x_v20100803.jar").noUpdate(),
        bundle(EQUINOX_HOME + "org.eclipse.equinox.util_1.0.200.v20100503.jar").noUpdate(),
        bundle(EQUINOX_HOME + "org.eclipse.osgi.services_3.2.100.v20100503.jar").noUpdate(),
        
        // Ant dependency of refit-osgi
        mavenBundle("org.apache.ant", "com.springsource.org.apache.tools.ant", "1.8.1"),
        
        equinox().version("3.6.0"),
        vmOption("-Drefit.root=" + System.getProperty("refit.root"))

        );
        return options;
    }
    
    
    @Test
    public void singleFileRunner() throws IOException, CommandLineException {
        FixtureLoader loader = getOsgiService(FixtureLoader.class);
        assertNotNull(loader);

        FixtureLoader.setInstance(loader);
        String inputDir = getInputDir();
        System.setProperty("fit.inputDir", inputDir);
        FileRunner runner = new FileRunner(new File(inputDir), new File(getOutputDir()), "MusicExample.html");
        boolean success = runner.run();
        assertTrue(success);        
    }
    
    @Test
    public void treeRunner() throws IOException, CommandLineException {
        FixtureLoader loader = getOsgiService(FixtureLoader.class);
        assertNotNull(loader);

        FixtureLoader.setInstance(loader);
        String inputDir = getInputDir();
        System.setProperty("fit.inputDir", inputDir);
        TreeRunner runner = new TreeRunner(new File(inputDir), new File(getOutputDir()), "MusicExample.html");
        boolean success = runner.run();
        assertTrue(success);
    }


    public String getInputDir() {
        String refitRoot = System.getProperty("refit.root");
        String inputDir = new File(refitRoot, "refit-eg/refit-eg-osgi-test/src/test/fit").getAbsolutePath();
        return inputDir;
    }
    
    public String getOutputDir() {
        String refitRoot = System.getProperty("refit.root");
        String inputDir = new File(refitRoot, "refit-eg/refit-eg-osgi-test/target/fit").getAbsolutePath();
        return inputDir;
    }
    
    protected <T> T getOsgiService(Class<T> type) {
        return getOsgiService(type,  DEFAULT_TIMEOUT);
    }

    protected <T> T getOsgiService(Class<T> type, long timeout) {
        ServiceTracker tracker = null;
        try {
            tracker = new ServiceTracker(bundleContext, type.getName(), null);
            tracker.open();
            Object svc = type.cast(tracker.waitForService(timeout));
            tracker.close();
            if (svc == null) {
                throw new RuntimeException("Gave up waiting for service " + type);
            }
            return type.cast(svc);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

