## Introduction ##

In Java EE, annotation-based dependency injection has been around since Java EE 5, and it has been extended and harmonized to some extent with the CDI standard introduced in Java EE 6.

Another new feature of Java EE 6 is the embedded EJB container API which lets you start a Java EE server and access its EJBs and other resources within the same Java VM instance. In many scenarios, this is a convenient method of testing your application, since you can rely on all the features provided by the container, like automatic transaction management, dependency injection, EJB pooling etc.

The [jeeunit](http://jeeunit.googlecode.com) project supports this testing approach by creating a WAR from all classes and resources on the classpath on the fly and deploying it to an embedded container.

Building on jeeunit, reFit allows you to create a WAR on the fly from your system under test, your fixtures and reFit itself to run Fit tests in an embedded Java EE 6 container.

## The Music Example Revisited ##

The [refit-eg-glassfish](http://code.google.com/p/refit/source/browse/refit-eg/refit-eg-glassfish) module contains an "enterprise version" of the music player scenario used in other reFit example projects.

`Music` is a JPA entity class and `MusicLibrary` is a stateless session bean reading `Music` entities from a real database initialized from a text file loaded from the Fit fixture. The session bean and the fixture classes all use CDI to inject their dependencies.

This is the code you need to run your Fit tests in an embedded container:

```
public class EmbeddedGlassfishFitRunner implements Runnable {

    @Override
    public void run() {
        ContainerLauncher launcher = ContainerLauncherLookup.getContainerLauncher();
        launcher.launch();
        launcher.autodeploy();

        Client client = Client.create();
        URI uri = launcher.getContextRootUri().resolve("fitrunner");
        WebResource testRunner = client.resource(uri);

        try {
            String result = testRunner 
                    .queryParam("inputDir", "src/test/fit") 
                    .queryParam("outputDir", "target/fit").get(String.class);
            System.out.println("Result from FitRunnerServlet: " + result);
        }
        catch (UniformInterfaceException exc) {
            exc.printStackTrace();
        }
        launcher.shutdown();
    }
}
```

The entry point to Fit is implemented by a `FitRunnerServlet` which is invoked by an HTTP GET request.

## Supported Containers ##

As you can see, there are no container-specific API calls in the example. `ContainerLauncher` is a service provider interface which lets you select the desired implementation by simply adding the correct libraries to the classpath.

At the moment, jeeunit only supports Glassfish 3.1-b33 or higher. The required dependencies are:

```
    <dependency>
      <groupId>com.googlecode.refit</groupId>
      <artifactId>refit-cdi</artifactId>
      <version>1.6.0</version>
    </dependency>
    <dependency>
      <groupId>org.glassfish.extras</groupId>
      <artifactId>glassfish-embedded-all</artifactId>
      <version>3.1-b33</version>
    </dependency>
    <dependency>
      <groupId>com.googlecode.jeeunit</groupId>
      <artifactId>jeeunit-glassfish</artifactId>
      <version>0.7.0</version>
    </dependency>
```