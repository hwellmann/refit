/*
 * Copyright 2013 Harald Wellmann
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
package com.googlecode.refit.paxexam;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

import com.googlecode.refit.cdi.CdiFixtureLoader;
import com.googlecode.refit.runner.TreeRunner;

import fit.FixtureLoader;

@RunWith(PaxExam.class)
public class PaxExamFitSuite {
    
    
    static {
        FixtureLoader.setInstance(new CdiFixtureLoader());
    }

    @Test
    public void runSuite() {
        File inputDir = new File("src/test/fit");
        File outputDir = new File("target/fit");
        String[] includes = new String[] { "**/*.html" };
        String[] excludes = null;

        TreeRunner runner = new TreeRunner(inputDir, outputDir, includes, excludes);
        
        runner.run();
    }

}
