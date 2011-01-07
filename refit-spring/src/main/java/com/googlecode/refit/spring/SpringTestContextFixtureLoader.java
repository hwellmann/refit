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

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

import fit.Fixture;
import fit.FixtureLoader;

/**
 * A fixture loader which performs Spring dependency injection on all fixtures annotated with
 * {@link ContextConfiguration}, using a Spring Test Context.
 * 
 * For classes without a {@link ContextConfiguration} annotation, this loader defaults to the
 * standard {@link FixtureLoader} behaviour.
 * 
 * @author Harald Wellmann
 * 
 */
public class SpringTestContextFixtureLoader extends FixtureLoader {

    /**
     * Creates an instance of a fixture class and performs Spring dependency injection if class is
     * annotated with {@link ContextConfiguration}.
     * 
     * @param fixtureClassName
     *            name of fixture class
     */
    @Override
    public Fixture createFixture(Class<?> klass) throws InstantiationException,
            IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();

        ContextConfiguration cc = klass.getAnnotation(ContextConfiguration.class);
        if (cc != null) {
            TestContextManager contextManager = new TestContextManager(klass);
            try {
                contextManager.prepareTestInstance(fixture);
            }
            catch (Exception exc) {
                throw new InstantiationException("dependency injection failed for "
                        + klass.getName());
            }
        }
        return fixture;
    }
}
