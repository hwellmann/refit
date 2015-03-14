## Introduction ##

To let Spring inject your fixture dependencies, you need to provide an application context. reFit supports both standard application contexts and test contexts.

In either case, your fixtures will be injected with beans from the context, but the fixtures will not be automatically registered as beans. If you want to inject your fixtures into each other, you need to set them up as beans in your application context.

Working with a standard application context, all fixtures will be injected from the same context.

With a test context, you can use different contexts for different (groups of) fixtures in your suite, the contexts will be cached and reused behind the scenes by a Spring `TestContextManager`.

For using a test context, you need to annotate each fixture class with a `@ContextConfiguration` indicating the context location. A fixture class without this annotation will fall back to POJO behaviour.

## Using the `FileRunner` with Spring programmatically ##

If you use the `fit.FileRunner` programmatically to run Fit tests, you can make it Spring-aware by installing a non-standard `FixtureLoader`. To work with a standard application context, first build the context and then invoke

```
FixtureLoader.setInstance(new SpringApplicationContextFixtureLoader(context));
```

before using the `FileRunner`.

To work with the Spring Test Context, use another loader:

```
FixtureLoader.setInstance(new SpringTestContextFixtureLoader());
```


## Using a JUnit Wrapper ##

The `SpringTestSuite` is a specialization of the [FitSuite JUnit runner](FitWithJUnit.md) which sets up the Spring application or test context for you:

```
@RunWith(SpringFitSuite.class)
@FitConfiguration(DefaultFitConfiguration.class)
@ContextConfiguration("/META-INF/spring/fit-context.xml")
public class MusicAppFitSuite {

}
```


Putting a `@ContextConfiguration` annotation on your suite class, your suite will run with a standard application context.

Leaving out this annotation, the suite will use a test context for each fixture class with a `@ContextConfiguration` annotation.

Of course you can override the `DefaultFitConfiguration` in both cases, as shown in the [FitSuite](FitWithJUnit.md) example.

## Using Maven ##

To run a Spring-enabled Fit test suite from a Maven plugin, add the following to your POM:

```
      <plugin>
        <groupId>com.googlecode.refit.maven</groupId>
        <artifactId>refit-spring-maven-plugin</artifactId>
        <version>${refit.version}</version>
        <configuration>
          <sourceDirectory>${basedir}/src/test/fit</sourceDirectory>
          <sourceIncludes>**/*.fit.html</sourceIncludes>
          <outputDirectory>${project.build.directory}/fit</outputDirectory>
        </configuration>
        <executions>
          <execution>
            <phase>integration-test</phase>
            <goals>
              <goal>run</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

Depending on your application, you may have to add additional dependencies to the plugin, since a Maven plugin runs in its own classloader and does not have access the compile or test classpath of your project.

Internally, the refit-spring-maven-plugin puts your project's test class path it the thread context class loader, but even this does not cover all cases.

For similar reasons, the plugin currently only supports the test context mode. Further investigation is required to see if and how this restriction can be lifted.