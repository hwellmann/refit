## Fit in a Nutshell ##

### Testing by Tables ###

The following pictures should be enough to get the idea. You simply write a document with test tables with some inputs and expected outputs, like this:

![http://wiki.refit.googlecode.com/hg/img/BeforeCityFixture.png](http://wiki.refit.googlecode.com/hg/img/BeforeCityFixture.png)

After running the test document through Fit, you find matches highlighted in green and failures highlighted in red.

![http://wiki.refit.googlecode.com/hg/img/CityFixture.png](http://wiki.refit.googlecode.com/hg/img/CityFixture.png)

All the magic is performed by the Fit framework and a user-defined **fixture class**, listed in the first row of the table.

### Specification by Example ###

A Fit test is just a plain old HTML document with some tables. You can use as much text and diagrams as you like to write a specification, and then you insert some tables with examples, standard ones and corner cases, to turn your specification into an executable test specification.

Of course, you also need to code the fixture classes or have a developer do it for you.

### Testing for Domain Experts ###

JUnit, TestNG etc. are test frameworks for software developers. Fit is a test method mainly for the domain experts, because all the code is hidden. Using Fit tables, both domain experts and developers can check whether or not a system meets the requirements.