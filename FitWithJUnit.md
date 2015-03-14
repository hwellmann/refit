## Introduction ##

For a developer, the usability of any testing framework depends not only on the features of the framework itself, but also on the availability of integrations into IDEs, build tools, continuous integration servers etc.

As a pragmatic alternative to implementing the desirable integrations of reFit and X for all X, a JUnit wrapper for Fit is a useful first step, given that JUnit is integrated with almost all popular Java development tools.


## An Example ##

Suppose you have a Maven-compatible project structure with a bunch of Fit tests under `src/test/fit` with naming convention `*.fit.html`, and you want to run these tests and collect the processed output in `target/fit`.

Then the following JUnit suite will do the job:

```
@RunWith(FitSuite.class)
@FitConfiguration(MusicFitSuite.Configuration.class)
public class MusicFitSuite {

    public static class Configuration extends DefaultFitConfiguration {

        @Override
        public String getInputDir() {
            return "src/test/fit";
        }

        @Override
        public String getOutputDir() {
            return "target/fit";
        }
        
        @Override
        public String[] getIncludes() {
            return new String[] { "**/*.fit.html" };
        }
    }
}

```

The `getIncludes()` method returns a list of Ant file patterns to be matched against the files under the directory returned by `getInputDir()`.

## Running a JUnit Fit Suite in Eclipse ##

Once you have created a JUnit Fit suite, simply select it in the Package Explorer and invoke **Run As... | JUnit Test** from the context menu. You can watch the progress and the results of your suite in the JUnit view:

![http://wiki.refit.googlecode.com/hg/img/RefitJUnit.png](http://wiki.refit.googlecode.com/hg/img/RefitJUnit.png)

## Running a JUnit Fit Suite with Maven ##

By configuring the `maven-surefire-plugin` to include your JUnit Fit suites, you can run them like any other JUnit tests, and you will get the familiar reports:


```
[INFO] --- maven-surefire-plugin:2.6:test (default-test) @ refit-eg-plain ---
[INFO] Surefire report directory: /home/hwellmann/work/refit/refit-eg/refit-eg-plain/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running com.googlecode.refit.eg.music.suite.MusicFitSuite
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.214 sec
Running com.googlecode.refit.eg.music.suite.MusicFitSuiteWithErrors
Tests run: 4, Failures: 1, Errors: 1, Skipped: 0, Time elapsed: 0.152 sec <<< FAILURE!

Results :

Failed tests: 
  MusicExampleWithFailures.html

Tests in error: 
  MusicExampleWithErrors.html

Tests run: 6, Failures: 1, Errors: 1, Skipped: 0

[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
```