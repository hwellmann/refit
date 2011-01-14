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