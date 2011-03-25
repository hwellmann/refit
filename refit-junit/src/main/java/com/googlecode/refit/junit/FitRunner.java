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

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.googlecode.refit.runner.FileRunner;
import com.googlecode.refit.runner.ReportIO;
import com.googlecode.refit.runner.RunnerListener;
import com.googlecode.refit.runner.jaxb.TestResult;

/**
 * Runs a single FIT test under JUnit. Do not directly use this class in applications.
 * 
 * @see FitSuite
 * @author hwellmann
 *
 */
public class FitRunner extends Runner implements RunnerListener {
   
    /**
     * Description of this FIT test for JUnit notifiers. Corresponds to {@code testPath}.
     */
    private Description description;
    
    private FileRunner fileRunner;

    private RunNotifier notifier;

    private RunnerListener listener;
    
    public FitRunner(File inputDir, File outputDir, String testPath, RunnerListener listener) {
        System.setProperty("fit.currentTest", testPath);
        this.description = Description.createSuiteDescription(testPath);
        this.fileRunner = new FileRunner(inputDir, outputDir, testPath, this);
        this.listener = listener;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.fireTestStarted(getDescription());
        try {
            this.notifier = notifier;
            fileRunner.run();
        }
        catch (Exception exc) {
            Failure failure = new Failure(getDescription(), exc);
            notifier.fireTestFailure(failure);
        }
        notifier.fireTestFinished(getDescription());
    }

    /**
     * Returns an AssertionError for failed tests and an Exception for tests with unexpected
     * exceptions, to match JUnit conventions.
     * @return Throwable
     */
    private Throwable getThrowable(TestResult testResult) {
        String msg = ReportIO.format(testResult);
        if (testResult.getExceptions() > 0) {
            return new Exception(msg);
        }
        else
            return new AssertionError(msg);
    }

    @Override
    public void beforeTest(String testPath) {
        listener.beforeTest(testPath);
    }

    @Override
    public void afterTest(TestResult result) {
        listener.afterTest(result);
        if (result.getExceptions() > 0 || result.getWrong() > 0) {
            /*
             * Distinguish failed tests from tests in error by including the appropriate
             * type of Throwable in the Failure.
             */
            Failure failure = new Failure(getDescription(), getThrowable(result));
            notifier.fireTestFailure(failure);
        }
    }

    @Override
    public void afterSuite() {
        listener.afterSuite();
    }
}
