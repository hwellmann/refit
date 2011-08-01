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
package com.googlecode.refit.mojo.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.googlecode.refit.mojo.ClasspathClassLoader;
import com.googlecode.refit.mojo.FitRunnerMojo;
import com.googlecode.refit.spring.SpringApplicationContextFixtureLoader;
import com.googlecode.refit.spring.SpringTestContextFixtureLoader;

import fit.FixtureLoader;

/**
 * Mojo to run FIT tests which supports Spring dependency injection for fixtures.
 * 
 * The fixture classes to be handled by spring have be annotated with 
 * {@link ContextConfiguration}, specifying the location of the Spring XML configuration.
 * 
 * NOTE: By default, properties from parent Mojos are not visible to the Javadoc annotation
 * processor on derived Mojos. The maven-inherit-plugin relieves this restriction by providing
 * the {@code extendsPlugin} annotation, instructing the plugin builder to include the 
 * inherited properties.
 * 
 * @author Harald Wellmann
 * 
 * @extendsPlugin refit
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 */
public class SpringFitRunnerMojo extends FitRunnerMojo {
    
    /**
     * EXPERIMENTAL, DO NOT USE.
     * <p>
     * The location of an XML Spring context definition for a standard application context.
     * If this is parameter is missing, the plugin will use the Spring Test Context and build
     * contexts for all fixture classes annotated with {@code ContextConfiguration}.
     * 
     * @parameter
     */
    protected String applicationContext;

    @Override
    protected ClasspathClassLoader getTestClassLoader() {
        if (applicationContext == null) {
            FixtureLoader.setInstance(new SpringTestContextFixtureLoader());
        }
        else {
            ApplicationContext context = new GenericXmlApplicationContext(applicationContext);
            FixtureLoader.setInstance(new SpringApplicationContextFixtureLoader(context));
        }
        return super.getTestClassLoader();
    }
}
