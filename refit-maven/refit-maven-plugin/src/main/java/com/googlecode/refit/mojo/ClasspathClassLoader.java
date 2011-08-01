package com.googlecode.refit.mojo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import fit.Fixture;

/**
 * Specialization of URLClassLoader, interpreting a list of Strings as file URLs.
 * 
 * @author Mauro Talevi, Harald Wellmann
 */
public class ClasspathClassLoader extends URLClassLoader {

    public ClasspathClassLoader() {
        this(new URL[] {});
    }

    public ClasspathClassLoader(List<String> classpathElements, ClassLoader parent) throws MalformedURLException {
        this(toClasspathURLs(classpathElements), parent);
    }

    public ClasspathClassLoader(URL[] urls) {
        this(urls, Fixture.class.getClassLoader());
    }

    public ClasspathClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    protected static URL[] toClasspathURLs(List<String> classpathElements)
            throws MalformedURLException {
        List<URL> urls = new ArrayList<URL>();
        if (classpathElements != null) {
            for (String classpathElement : classpathElements) {
                urls.add(new File(classpathElement).toURI().toURL());
            }
        }
        return urls.toArray(new URL[urls.size()]);
    }
}
