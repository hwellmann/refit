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

import static com.googlecode.refit.osgi.util.ReFitProperties.getRoot;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;

import com.googlecode.refit.osgi.FileRunner;
import com.googlecode.refit.osgi.TreeRunner;

import fit.CommandLineException;
import fit.FixtureLoader;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class MusicOsgiTest {

    public static final String EQUINOX_HOME = "http://archive.eclipse.org/equinox/drops/R-3.6.1-201009090800/";
    public static final String REFIT_VERSION = "1.8.0-SNAPSHOT";

    @Inject
    private BundleContext bundleContext;

    @Inject
    private FixtureLoader loader;

    @Configuration
    public static Option[] configuration() {

        return options(
            systemProperty("logback.configurationFile")
                .value("file:src/test/resources/logback.xml"),

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
            mavenBundle("org.apache.servicemix.bundles", "org.apache.servicemix.bundles.ant",
                "1.8.4_1"),

            // Logging    
            mavenBundle("org.slf4j", "slf4j-api", "1.6.4"),
            mavenBundle("ch.qos.logback", "logback-core", "1.0.6"),
            mavenBundle("ch.qos.logback", "logback-classic", "1.0.6"),

            junitBundles());
    }

    @Test
    public void singleFileRunner() throws IOException, CommandLineException {
        FixtureLoader.setInstance(loader);
        String inputDir = getInputDir();
        System.setProperty("fit.inputDir", inputDir);
        FileRunner runner = new FileRunner(new File(inputDir), new File(getOutputDir()),
            "MusicExample.html");
        boolean success = runner.run();
        assertTrue(success);
    }

    @Test
    public void treeRunner() throws IOException, CommandLineException {
        FixtureLoader.setInstance(loader);
        String inputDir = getInputDir();
        System.setProperty("fit.inputDir", inputDir);
        TreeRunner runner = new TreeRunner(new File(inputDir), new File(getOutputDir()),
            "MusicExample.html");
        boolean success = runner.run();
        assertTrue(success);
    }

    private String getInputDir() {
        File refitRoot = getRoot();
        String inputDir = new File(refitRoot, "refit-eg/refit-eg-osgi-test/src/test/fit")
            .getAbsolutePath();
        return inputDir;
    }

    private String getOutputDir() {
        String refitRoot = System.getProperty("refit.root");
        String inputDir = new File(refitRoot, "refit-eg/refit-eg-osgi-test/target/fit")
            .getAbsolutePath();
        return inputDir;
    }
}
