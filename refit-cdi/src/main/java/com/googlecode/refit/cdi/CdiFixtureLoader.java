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
package com.googlecode.refit.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

import com.googlecode.jeeunit.BeanManagerLookup;

import fit.Fixture;
import fit.FixtureLoader;

/**
 * A fixture loader which performs CDI dependency injection on all fixtures.
 * 
 * @author Harald Wellmann
 *
 */
public class CdiFixtureLoader extends FixtureLoader {

    /**
     * Creates an instance of a fixture class and performs CDI dependency injection.
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
