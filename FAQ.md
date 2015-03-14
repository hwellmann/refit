### How does reFit relate to Fit? ###

The initial source baseline of reFit was taken from the latest source snapshot of the Fit project from [SourceForge](http://sourceforge.net/projects/fit) dating from 2008. The last Fit release 1.1 was in 2005. The Fit head revision and thus the reFit baseline contains some fixes and improvements that were never released, as far as I know.

The reFit 1.5.0 release exactly corresponds to this baseline, adding nothing to the sources except an updated version and copyright statement. The only changes are a Maven-friendly directory structure, Maven POMs and deployment to public Maven repositories (Sonatype OSS and Maven Central).

Starting with the 1.6.0 release, there is a growing number of [additional modules](ModuleList.md).

### Why does the release numbering start at 1.5.0? ###

reFit is based on Fit 1.1+, so the reFit baseline should be a value >= 1.2. I'm planning to follow the major.minor.patch convention for release numbers. 1.5.0 seems to be a reasonable offset from Fit.

### Why does reFit build on Fit rather than Fitnesse? ###

Fitnesse includes a modified version of the original Fit sources and adds a lot of new functionality. It lets you write and run tests from a Wiki. Fitnesse tests are written in a Wiki syntax, not in HTML.

I currently work in a (closed source) project with a fairly large code base of Fit tests. I tried running these tests under the Fitnesse version of Fit, only to find out that Fitnesse is not backward compatible and breaks our existing tests.

Apart from that, I do not think that all changes made to Fit in Fitnesse are improvements, but this is of course a matter of priorities and taste.

Some of the additional fixture base classes and type adapters from Fitnesse will probably make their way into reFit sooner or later.

### What is the roadmap for reFit? ###

See the list of open enhancement requests in the
[Issue Tracker](http://code.google.com/p/refit/issues/list?can=2&q=Type%3DEnhancement+&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).