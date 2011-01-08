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
