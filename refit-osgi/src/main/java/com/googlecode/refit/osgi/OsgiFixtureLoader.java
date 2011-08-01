package com.googlecode.refit.osgi;
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
import java.util.HashMap;
import java.util.Map;

import fit.ActionFixture;
import fit.Fixture;
import fit.FixtureLoader;
import fit.Summary;

public class OsgiFixtureLoader extends FixtureLoader {
    
    private Map<String, Fixture> fixtureMap = new HashMap<String, Fixture>();
    
    public OsgiFixtureLoader() {
        addFixture(new ActionFixture());
        addFixture(new Summary());
    }
    
    protected void addFixture(Fixture fixture) {
        fixtureMap.put(fixture.getClass().getName(), fixture);        
    }
    
    protected void removeFixture(Fixture fixture) {
        fixtureMap.remove(fixture.getClass().getName());        
    }
    
    @Override
    public Fixture loadFixture(String className) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Fixture fixture = fixtureMap.get(className);
        if (fixture == null) {
            throw new InstantiationException("no Fixture Service Component of class " + className);
        }
        return fixture;
    }
}
