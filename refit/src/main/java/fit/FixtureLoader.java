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
package fit;


/**
 * Loads fixture classes and creates fixture instances. This is a singleton which can be
 * replaced by a derive object to override the class loading or instantiation behaviour.
 * 
 * @author Harald Wellmann
 *
 */
public class FixtureLoader {
    private static FixtureLoader instance;

    /**
     * Returns the singleton instance.
     * @return the fixture loader
     */
    public static FixtureLoader getInstance() {
        if (instance == null) {
            instance = new FixtureLoader();
        }

        return instance;
    }

    /**
     * Installs the singleton instance to be used. This should only be called to override
     * the default singleton by an instance of a derived class.
     * @param instance
     */
    public static void setInstance(FixtureLoader instance) {
        FixtureLoader.instance = instance;
    }

    /**
     * Loads a fixture class with the given class name. By default, the thread context class
     * loader is used. Override this to use other class loaders (e.g. in OSGi).
     * @param className fully qualified class name
     * @return fixture class
     * @throws ClassNotFoundException
     */
    public Class<?> loadFixtureClass(String className) throws ClassNotFoundException {
        Class<?> klass = Thread.currentThread().getContextClassLoader().loadClass(className);
        return klass;
    }
    
    /**
     * Creates an instance of the given fixture class. Override this to postprocess the fixture
     * instance, e.g. for dependency injection.
     * @param klass fixture class
     * @return fixture instance
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Fixture createFixture(Class<?> klass) throws InstantiationException, IllegalAccessException {
        Fixture fixture = (Fixture) klass.newInstance();
        return fixture;
    }
    
    public Fixture loadFixture(String className) 
        throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> klass = loadFixtureClass(className);
        return createFixture(klass);
    }
}
