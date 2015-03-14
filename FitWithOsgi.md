## Introduction ##

The default approach of Fit's `FixtureLoader` to load fixture classes by name does not work with OSGi where each bundle has its own classloader. And besides, it should be folklore these days that using `Class.forName()` is almost always a bad idea.

Rather than messing around with classloaders, reFit uses OSGi Declarative Services to publish all fixtures classes as services, both under `fit.Fixture` and under the derived class name.

The default `FixtureLoader` is superseded by an `OsgiFixtureLoader` which simply looks up the required service by class name.


## Fixtures as Service Components ##

To turn your fixture classes into Service components, add public or protected setter methods for the injected members (OSGi does not support `@Inject` annotations):

```
public class Display extends RowFixture {
    
    // service to be injected from system under test
    private MusicLibrary musicLibrary;

    protected void setMusicLibrary(MusicLibrary library) {
        this.musicLibrary = library;
    }

    // fixture methods    
}
```

Then create a Service Component definition for each fixture class:

```
<?xml version="1.0" encoding="UTF-8"?>
<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" name="Display">
   <implementation class="com.googlecode.refit.osgi.eg.music.fixtures.Display"/>
   <reference bind="setMusicLibrary" cardinality="1..1"
           interface="com.googlecode.refit.osgi.eg.music.MusicLibrary"
           name="MusicLibrary" policy="static"/>
   <service>
      <provide interface="fit.Fixture"/>
      <provide interface="com.googlecode.refit.osgi.eg.music.fixtures.Display"/>
   </service>
</scr:component>

```

Include a `<reference>` element for each injection point, and register your component with the service interfaces `fix.Fixture` and the actual implementation class. Note that an OSGi service "interface" is not necessarily a Java interface, any old class will do.

It is recommended to use the class name both as service component name and as base name for the descriptor. In this example, the definition for the fixture a.k.a service component named `Display` is placed in `OSGI-INF/Display.xml`.

Finally, add a header to your manifest to enable your bundle for Declarative Services:

```
Service-Component: OSGI-INF/*
```

## A Complete Example ##

There is another variant of the music player example specially customized for OSGi. The original version has lots of dependency cycles which cannot be handled by the service component runtime. The OSGi variant comes in three Maven modules: two bundles and a test project:

  * `refit-eg-osgi`: the system under test
  * `refit-eg-osgi-fixtures`: the fixture classes
  * `refit-eg-osgi-test`: a test project running reFit under Pax Exam

Pax Exam is a test framework for OSGi which starts an OSGi framework, installs and starts any number of bundles and then wraps your JUnit tests in another bundle constructed on the fly so that your tests run within the OSGi framework.

Have a look at [MusicOsgiTest](http://code.google.com/p/refit/source/browse/refit-eg/refit-eg-osgi-test/src/test/java/com/googlecode/refit/osgi/test/MusicOsgiTest.java) to see how to run a directory tree of Fit tests under OSGi:

```
    @Test
    public void treeRunner() throws IOException, CommandLineException {
        FixtureLoader loader = getOsgiService(FixtureLoader.class);
        assertNotNull(loader);

        FixtureLoader.setInstance(loader);
        String inputDir = getInputDir();
        System.setProperty("fit.inputDir", inputDir);
        TreeRunner runner = new TreeRunner(new File(inputDir), new File(getOutputDir()), "**/*.html");
        boolean success = runner.run();
        assertTrue(success);
    }
```