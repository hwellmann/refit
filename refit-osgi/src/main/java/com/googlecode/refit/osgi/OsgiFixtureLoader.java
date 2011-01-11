package com.googlecode.refit.osgi;

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
