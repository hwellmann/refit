package com.googlecode.refit.eg.music.suite;

import static com.googlecode.refit.eg.music.suite.ReFitProperties.getRoot;

import java.io.File;

import com.googlecode.refit.junit.DefaultFitConfiguration;

/**
 * Shared configuration for the Music example suites.
 * 
 * @author Harald Wellmann
 *
 */
public class MusicConfiguration extends DefaultFitConfiguration {

    @Override
    public String getInputDir() {
        return new File(getExampleRoot(), "src/test/fit").getAbsolutePath();
    }

    @Override
    public String getOutputDir() {
        return new File(getExampleRoot(), "target/fit").getAbsolutePath();
    }
    
    protected File getExampleRoot() {
        return new File(getRoot(), "refit-eg/refit-eg-plain");
    }
}