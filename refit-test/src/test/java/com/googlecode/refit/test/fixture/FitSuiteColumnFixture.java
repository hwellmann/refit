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
