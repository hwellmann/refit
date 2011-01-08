/*
 * Copyright 2010 Harald Wellmann
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

package com.googlecode.refit.test.suite;

import org.junit.runner.RunWith;

import com.googlecode.refit.junit.DefaultFitConfiguration;
import com.googlecode.refit.junit.FitConfiguration;
import com.googlecode.refit.junit.FitSuite;

/**
 * An example showing how to run a suite of FIT tests under JUnit.
 * 
 * @author hwellmann
 * 
 */
@RunWith(FitSuite.class)
@FitConfiguration(MetaFitSuite.Configuration.class)
public class MetaFitSuite {

    public static class Configuration extends DefaultFitConfiguration {

        @Override
        public String getInputDir() {
            return "src/test/fit";
        }

        @Override
        public String getOutputDir() {
            return "target/fit";
        }
        
        @Override
        public String[] getIncludes() {
            return new String[] { "**/FitSuite.fit.html" };
        }
    }
}
