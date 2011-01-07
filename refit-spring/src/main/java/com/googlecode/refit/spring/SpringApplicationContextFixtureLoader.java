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
