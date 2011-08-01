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

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

import fit.Fixture;
import fit.FixtureLoader;

/**
 * A fixture loader which takes a Spring application context and performs dependency injection on
 * all fixtures, using the given context.
 * 
 * @author Harald Wellmann
 * 
 */
public class SpringApplicationContextFixtureLoader extends FixtureLoader {

    private ApplicationContext context;

    public SpringApplicationContextFixtureLoader(ApplicationContext context) {
        this.context = context;
    }

    /**
     * Creates an instance of a fixture class and performs Spring dependency injection on this
     * instance.
     * 
     * @param fixtureClassName
     *            name of fixture class
     */
    @Override
    public Fixture createFixture(Class<?> klass) throws InstantiationException,
            IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();

        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(fixture, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(fixture, klass.getName());

        return fixture;
    }
}
