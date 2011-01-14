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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import fit.RowFixture;

public class FitSuiteRowFixture extends RowFixture {


    @Override
    public Object[] query() throws Exception {
        Class<?> klass = getClass().getClassLoader().loadClass(args[0]);

        JUnitCore core = new JUnitCore();
        FitSuiteRunListener listener = new FitSuiteRunListener();
        core.addListener(listener);
        core.run(klass);
        return listener.getResults().toArray();
    }

    @Override
    public Class<?> getTargetClass() {
        return JUnitResult.class;
    }

    private static class FitSuiteRunListener extends RunListener {
        
        private Map<String, JUnitResult> results = new LinkedHashMap<String, JUnitResult>();
        
        @Override
        public void testFinished(Description description) throws Exception {
            String name = description.getDisplayName();
            if (results.containsKey(name)) 
                return;
            JUnitResult result = new JUnitResult();
            result.name = name;
            result.status = "passed";
            
            results.put(name, result);
            
            
        }

        @Override
        public void testFailure(Failure failure) throws Exception {
            String name = failure.getDescription().getDisplayName();
            String status = getStatus(failure);
            JUnitResult result = new JUnitResult();
            result.name = name;
            result.status = status;
            
            results.put(name, result);
        }

        private String getStatus(Failure failure) {
            if (failure.getException() instanceof AssertionError) {
                return "failed";
            }
            else {
                return "error";
            }
        }
        
        public Collection<JUnitResult> getResults() {
            return results.values();
        }
    }
    
    public static class JUnitResult {
        public String name;
        public String status;        
    }
}
