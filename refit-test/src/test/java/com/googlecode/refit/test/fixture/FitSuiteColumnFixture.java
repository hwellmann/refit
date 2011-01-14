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
package com.googlecode.refit.test.fixture;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import fit.ColumnFixture;

public class FitSuiteColumnFixture extends ColumnFixture {
    
    public String className;
    
    public int total;
    public int passed;
    public int failed;
    public int errors;
    
    
    
    
    public int getTotal() {
        return total;
    }


    public int getPassed() {
        return passed;
    }


    public int getFailed() {
        return failed;
    }


    public int getErrors() {
        return errors;
    }


    @Override
    public void reset() throws Exception {
        total = 0;
        passed = 0;
        failed = 0;
        errors = 0;
    }
    
    
    @Override
    public void execute() throws Exception {
        
        Class<?> klass = getClass().getClassLoader().loadClass(className);
        Result result = JUnitCore.runClasses(klass);
        total = result.getRunCount();
        for (Failure failure : result.getFailures()) {
            if (failure.getException() instanceof AssertionError) {
                failed++;
            }
            else
            {
                errors++;
            }
        }
        passed = total - failed - errors;
    }

}
