## Introduction ##

CDI does not require a Java EE container, it can also be used in Java SE applications, comparable to other dependency injection frameworks like Guice or Spring.

## An Example ##

Another variant of the Music Player example can be found in refit-eg-cdi-weld, using the Weld SE CDI implementation. The code is almost the same as in the Spring example, except for a few annotations.

Dependency injection is performed by a `CdiFixtureLoader` which internally uses the CDI `BeanManager`.

As in the Java EE case, any dependencies on a specific CDI implementation are encapsulated by [jeeunit](http://jeeunit.googlecode.com) helper classes.

## Using the `FileRunner` with CDI programmatically ##

If you use the `fit.FileRunner` programmatically to run Fit tests, you can make it CDI-aware by installing a non-standard `FixtureLoader` before using the `FileRunner`:

```
FixtureLoader.setInstance(new CdiFixtureLoader(context));
```


## Using a JUnit Wrapper ##

The `CdiFitSuite` is a specialization of the [FitSuite JUnit runner](FitWithJUnit.md) which enables CDI for you:

```
@RunWith(CdiFitSuite.class)
@FitConfiguration(DefaultFitConfiguration.class)
public class MusicWeldFitSuite {

}
```


Of course you can override the `DefaultFitConfiguration`, as shown in the [FitSuite](FitWithJUnit.md) example.

## Required Libraries ##

To use reFit with Weld in your own projects, include the following dependencies:

```
    <dependency>
      <groupId>com.googlecode.refit</groupId>
      <artifactId>refit-cdi</artifactId>
      <version>${refit.version}</version>
    </dependency>
    <dependency>
      <groupId>com.googlecode.jeeunit</groupId>
      <artifactId>jeeunit-weld-se</artifactId>
      <version>0.7.0</version>
    </dependency>
```