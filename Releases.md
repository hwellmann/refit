## Maven Artifacts ##

reFit is built and released with Maven. reFit releases get promoted to Maven Central via Sonatype OSS Repository hosting.

To use reFit in your own project, add this dependency to your POM:

```
    <dependency>
      <groupId>com.googlecode.refit</groupId>
      <artifactId>refit</artifactId>
      <version>1.6.0</version>
    </dependency>
```


To access snapshots or staged releases, add the Sonatype OSS Staging repository to your Maven settings:

```
    <repository>
      <id>sonatype-oss-staging</id>
      <name>Sonatype OSS Staging Repository</name>
      <url>http://oss.sonatype.org/content/groups/staging</url>
    </repository>
```

Starting from release 1.6.0, reFit has a number of additional integration modules for other frameworks. See the [module list](ModuleList.md) for details.

## Downloads on Google Code ##

Maven is the primary distribution platform for reFit. If you do not use Maven, get a `src` or `bin` package from the [Downloads](http://code.google.com/p/refit/downloads/list) page.

The `bin` packages contain all Core and Integration JARs, but no example code. The `src` packages contain the complete source tree.

There will be no downloads for development snapshots.