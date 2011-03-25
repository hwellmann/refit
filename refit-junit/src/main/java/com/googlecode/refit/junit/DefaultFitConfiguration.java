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

import com.googlecode.refit.runner.ReportGenerator;
import com.googlecode.refit.runner.RunnerListener;

public class DefaultFitConfiguration {

    public static final String INPUT_DIR = "src/test/fit";
    public static final String OUTPUT_DIR = "target/fit";
    public static final String INCLUDE_HTML = "**/*.html";

    public String getInputDir() {
        return INPUT_DIR;
    }

    public String getOutputDir() {
        return OUTPUT_DIR;
    }

    public String[] getIncludes() {
        return new String[] { INCLUDE_HTML };
    }

    public String[] getExcludes() {
        return new String[0];
    }
    
    public RunnerListener getRunnerListener() {
        return new ReportGenerator(new File(getInputDir()), new File(getOutputDir()));
    }
}
