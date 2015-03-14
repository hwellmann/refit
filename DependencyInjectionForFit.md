## Introduction ##

Today, most software developers are familiar with the concept of Dependency Injection, but this must have been different in 2002, when the [Fit](http://fit.c2.com/) framework was created by Ward Cunningham.

The fixture classes for a Fit test need to interact with the business objects of the system under test. In the original Fit examples, the fixtures mostly use static lookup methods to access singleton business objects, or they create their own object graph of business objects.

When the system under test uses some sort of dependency injection mechanism, it is only natural to apply the same mechanism to all test code for the given system.

## An Example ##

The [refit-eg-spring](http://code.google.com/p/refit/source/browse/refit-eg/refit-eg-spring) project contains a modified version of the original `MusicPlayer` example where all static references are replaced by field injection, like this:

```
public class Browser extends Fixture {

    @Inject
    private MusicLibrary musicLibrary;

    @Inject
    private MusicPlayer musicPlayer;
    
    // Library //////////////////////////////////

    public void library (String path) throws Exception {
        musicLibrary.load(path);
    }

    public int totalSongs() {
        return musicLibrary.library.length;
    }

    // Select Detail ////////////////////////////

    public String playing () {
        return musicPlayer.playing.title;
    }

```

To make this work, the dependency injection framework has to hook into the Fit runner to wire up all injection points as soon as the fixture instance is created.

As of release 1.6.0, reFit supports [Java EE 6](FitWithJavaEE6.md), [Spring](FitWithSpring.md), [OSGi](FitWithOsgi.md) and [CDI/Weld SE](FitWithCdi.md) as dependency injection containers.

The reFit project follows the JSR-330 standard as closely as possible and avoids the use of non-standard annotations or other injection mechanisms, so any JSR-330 compliant dependency injection container (e.g. Guice) can be supported with minimum effort.