/*
 * Copyright 2011 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.googlecode.refit.spring;

import org.junit.runners.model.InitializationError;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.googlecode.refit.junit.FitSuite;

import fit.FixtureLoader;

/**
 * Use this class to run a suite of FIT tests under JUnit. To do so, create a class with the
 * following annotations:
 * <pre>
 * &#64;RunWith(SpringFitSuite.class)
 * &#64;FitConfiguration(
 *     inputDir = "src/test/fit", 
 *     outputDir = "target/fit", 
 *     includes = "**&#47;*.fit.html")
 * public class MyFitSuite {
 * }
 * </pre>
 * 
 * The suite first builds a list of input files to be run as FIT tests. The input files are
 * located under the {@code inputDir} root. The file patterns to be included or excluded are specified
 * by the {@code includes} and {@code excludes} properties. The default include pattern is
 * {@code **&#47;*.html}, the exclude pattern list is empty by default.
 * Each of these properties can be a list of Strings
 * interpreted as Ant file patterns.
 * <p>
 * The suite then runs all files matching at least one of the include patterns and none of the
 * exclude patterns.
 * <p>
 * Each test file will be a direct child of the suite, named by its relative path.
 * <p>
 * Each fixture class referenced from the FIT test files needs to be annotated with 
 * {@link ContextConfiguration} to specify the Spring context configuration. Spring dependency
 * injection will be performed automatically after fixture instantiation.
 * 
 * @author hwellmann
 *
 */
public class SpringFitSuite extends FitSuite {

    public SpringFitSuite(Class<?> klass) throws InitializationError {
        super(klass);
        
        ContextConfiguration cc = klass.getAnnotation(ContextConfiguration.class);
        if (cc == null) {
            FixtureLoader.setInstance(new SpringTestContextFixtureLoader());
        }
        else {
            ApplicationContext context = new GenericXmlApplicationContext(cc.value());
            FixtureLoader.setInstance(new SpringApplicationContextFixtureLoader(context));
        }
    }
}
