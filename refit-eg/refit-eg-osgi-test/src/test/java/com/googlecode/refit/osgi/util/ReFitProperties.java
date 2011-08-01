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
package com.googlecode.refit.osgi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * Helper class for persisting the root of the reFit source tree. Used for reFit self-tests when
 * reFit spawns itself in another process.
 * <p>
 * Properties are persisted in ${user.home}/.refit/refit.properties. 
 * 
 * @author Harald Wellmann
 *
 */
public class ReFitProperties {
       
    public static final String REFIT_PROPERTIES = "refit.properties";
    public static final String ROOT_KEY = "refit.root";
    
    private static File refitRoot;

    public static File getRoot() {
        if (refitRoot == null) {
            refitRoot = new File(".");
            File propDir  = new File(System.getProperty("user.home"), ".refit");
            File propFile = new File(propDir, REFIT_PROPERTIES);
            try {
                Properties props = new Properties();
                props.load(new FileReader(propFile));
                String root = props.getProperty(ROOT_KEY);
                if (root != null) {
                    refitRoot = new File(root);
                }
            }
            catch (FileNotFoundException e) {
                // ignore
            }
            catch (IOException e) {
                // ignore
            }
        }
        return refitRoot;
    }
    
    public static void setRoot(File root) throws IOException {
        refitRoot = root;
        File propDir  = new File(System.getProperty("user.home"), ".refit");
        File propFile = new File(propDir, REFIT_PROPERTIES);
        if (!propFile.exists()) {
            Properties props = new Properties();
            props.put(ROOT_KEY, root.getAbsolutePath());
            FileWriter writer = new FileWriter(propFile);
            props.store(writer, "reFit properties");
        }
    }
}
