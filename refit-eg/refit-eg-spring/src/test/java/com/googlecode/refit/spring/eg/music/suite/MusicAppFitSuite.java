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
package com.googlecode.refit.spring.eg.music.suite;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.googlecode.refit.junit.DefaultFitConfiguration;
import com.googlecode.refit.junit.FitConfiguration;
import com.googlecode.refit.spring.SpringFitSuite;

/**
 * An example showing how to run a Spring-enabled suite of FIT tests under JUnit.
 * 
 * @author hwellmann
 * 
 */
@RunWith(SpringFitSuite.class)
@FitConfiguration(MusicAppFitSuite.Configuration.class)
@ContextConfiguration("/META-INF/spring/fit-context.xml")
public class MusicAppFitSuite {

    public static class Configuration extends DefaultFitConfiguration {

        @Override
        public String getInputDir() {
            return "src/test/fit";
        }

        @Override
        public String getOutputDir() {
            return "target/fit";
        }
    }
}
