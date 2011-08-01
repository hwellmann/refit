/*
 * Copyright 2011 Harald Wellmann
 *
 * This file is part of reFit.
 * 
 * reFit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * reFit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with reFit.  If not, see <http://www.gnu.org/licenses/>.
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
