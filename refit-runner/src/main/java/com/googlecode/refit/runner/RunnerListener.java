package com.googlecode.refit.runner;

import com.googlecode.refit.runner.jaxb.TestResult;

public interface RunnerListener {

    public void beforeTest(String testPath);
    public void afterTest(TestResult result);
}
