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
package com.googlecode.refit.runner;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.refit.runner.jaxb.TestResult;

public class CompositeRunnerListener implements RunnerListener {
    
    private List<RunnerListener> listeners = new ArrayList<RunnerListener>();
    
    public void addListener(RunnerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RunnerListener listener) {
        listeners.remove(listener);
    }

    public void beforeTest(String testPath) {
        for (RunnerListener listener : listeners) {
            listener.beforeTest(testPath);
        }
    }
    
    public void afterTest(TestResult result) {
        for (RunnerListener listener : listeners) {
            listener.afterTest(result);
        }        
    }

    public void afterSuite() {
        for (RunnerListener listener : listeners) {
            listener.afterSuite();
        }        
    }
}
