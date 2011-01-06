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
package com.googlecode.refit.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

import com.googlecode.jeeunit.BeanManagerLookup;

import fit.Fixture;
import fit.FixtureLoader;

/**
 * A fixture loader which performs Spring dependency injection on all fixtures annotated
 * with {@link ContextConfiguration}, using a Spring Test Context.
 * 
 * @author Harald Wellmann
 *
 */
public class CdiFixtureLoader extends FixtureLoader {

    /**
     * Creates an instance of a fixture class and performs Spring dependency injection
     * if class is annotated with {@link ContextConfiguration}.
     * 
     * @param fixtureClassName name of fixture class
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Fixture createFixture(Class<?> klass) throws InstantiationException,
            IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();

        BeanManager mgr = BeanManagerLookup.getBeanManager();
        AnnotatedType annotatedType = mgr.createAnnotatedType(klass);
        InjectionTarget target = mgr.createInjectionTarget(annotatedType);
        CreationalContext context = mgr.createCreationalContext(null);
        target.inject(fixture, context);
        return fixture;
    }    
}
