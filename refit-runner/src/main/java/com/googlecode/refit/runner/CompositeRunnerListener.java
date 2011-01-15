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
}
