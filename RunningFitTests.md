## Overview ##

This page explains how to run Fit tests with reFit.

You can

  * run a single test from the command line using `fit.FileRunner`
  * run tests programmatically using `FileRunner` or `TreeRunner`.
  * run a set of Fit tests as a JUnit suite
  * run a set of Fit tests from Maven


## Running a Fit Test from the Command Line ##

If you are familiar with Fit at all, you can use your existing fixtures and Fit tests and run them with the [FileRunner](http://code.google.com/p/refit/source/browse/refit/src/main/java/fit/FileRunner.java), invoking it as follows:

```
java fit.FileRunner input-file output-file
```


## Running a Fit Test Programmatically ##

The `com.googlecode.refit.runner` package in module `refit-runner` contains classes `FileRunner` and `TreeRunner` for running a single test or a suite of tests from a given directory tree.

These classes are used internally by all the wrappers and adapters introduced below, and you can use them directly from your own application code, if you like.

Both of these classes take a `RunnerListener` argument. This listener will be notified on completion of individual tests and of the entire suite. A special listener is the `ReportGenerator` which creates HTML and XML summaries for a test suite.

You can create your own listeners by implementing `RunnerListener` or extending the no-op `DefaultRunnerListener`. Multiple listeners can be combined with a `CompositeRunnerListener`.

## Runner Options ##

The `TreeRunner` arguments are used with the same syntax and semantics for the JUnit and Maven wrappers as well:

  * inputDir: the root of the Fit test directory tree
  * outputDir: the root of the Fit test result tree
  * includes: a list of Ant file patterns specifying the set of input files, relative to `inputDir`. The default value is `**/*.html`
  * excludes: a list of Ant file patterns specifying files to be excluded from the test suite. The default is empty, i.e. all files matching the `includes` pattern will be run.

## Running a Fit Test Suite via JUnit ##

Using a custom runner for JUnit, you can wrap a Fit test suite in a JUnit suite and launch it via any JUnit runner, e.g. using the JUnit support of Eclipse.

All you need is an empty Java class with a custom JUnit runner:

```
@RunWith(FitSuite.class)
public class MusicFitSuite {

}

```

This will use a default configuration of

```
inputDir = src/test/fit
outputDir = target/fit
includes = **/*.html
```

To override the default, extend `DefaultFitConfiguration` and annotate your suite class like this:

```
@RunWith(FitSuite.class)
@FitConfiguration(MusicFitSuite.Configuration.class)
public class MusicFitSuite {

    public static class Configuration extends DefaultFitConfiguration {

        @Override
        public String[] getIncludes() {
            return new String[] { "**/*.fit.html" };
        }
    }
}
```

`FitSuite` is a helper class provided by reFit. To use it in your own project, simply include the following dependency:

```
    <dependency>
      <groupId>com.googlecode.refit</groupId>
      <artifactId>refit-junit</artifactId>
      <version>${refit.version}</version>
    </dependency>
```


## Running a Fit Test Suite via Maven ##

Using the `refit-maven-plugin`, you can run a suite of Fit tests in the `integration-test` phase of your Maven build:

```
  <build>
    <plugins>
      <plugin>
        <groupId>com.googlecode.refit.maven</groupId>
        <artifactId>refit-maven-plugin</artifactId>
        <version>${refit.version}</version>
        <configuration>
          <sourceDirectory>${basedir}/src/test/fit</sourceDirectory>
          <sourceIncludes>**/*.html</sourceIncludes>
          <outputDirectory>${project.build.directory}/fit</outputDirectory>
          <ignoreFailures>true</ignoreFailures>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

reFit's `refit-maven-plugin` is based on the original [Codehaus fit-maven-plugin](http://mojo.codehaus.org/fit-maven-plugin/), but it uses its own version of Fit, whereas the Codehaus plugin uses the official Fit 1.1 version from 2005 (or so).

## A Complete Example ##

The [refit-eg-plain](http://code.google.com/p/refit/source/browse/refit-eg/refit-eg-plain) subproject contains a ready-to-run example, based on the music player example from the original Fit distribution.