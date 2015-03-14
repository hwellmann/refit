# News #
## 24 Apr 2013: reFit 1.8.0 released ##

  * New system property `fit.keepSmartQuotes` to prevent Fit from replacing typographic quotes by straight quotes. The default is false.
  * Better Java EE support, jeeunit dependency replaced by Pax Exam 3.

## 1 Aug 2011: reFit 1.7.1 released ##

  * Fit test result reports now contain rows sums (total number of events per test).
  * POMs adapted to m2e 1.0 and Eclipse 3.7.0.

## 5 Apr 2011: reFit Jenkins Plugin 0.3 ##

  * Includes trend graph on project page.

## 28 Mar 2011: reFit Jenkins Plugin 0.2 ##

  * First release of [Jenkins plugin for reFit](https://wiki.jenkins-ci.org/display/JENKINS/reFit+Plugin).
  * Publishes Fit Test results on the project page.

## 28 Mar 2011: reFit 1.7.0 released ##

  * HTML and XML summaries of Fit test results.
  * New module refit-runner factors out common code for running fit tests.
  * Fit test execution can be monitored by a `RunnerListener`.

## 16 Jan 2011: reFit 1.6.0 released ##

  * This release integrates reFit with the following dependency injection containers:
    * Java EE 6
    * Spring 3
    * OSGi Declarative Services
> Using the appropriate reFit integration module, you can inject beans/services from
> the system under test into your fixture classes.

  * You can run Fit test suites under JUnit, using the `FitSuite` JUnit runner.

  * You can run Fit test suites from Maven, with or without Spring support, using the `refit-maven-plugin` or the `spring-refit-maven-plugin`, respectively.

## 6 Jan 2011: reFit 1.5.0 released ##

  * reFit 1.5.0 is the initial baseline release of reFit containing the latest Java sources from the SourceForge fit project. The only changes are the directory structure, Maven POMs, and the version information.

  * reFit artifacts are published to Maven Central via Sonatype OSS Hosting.