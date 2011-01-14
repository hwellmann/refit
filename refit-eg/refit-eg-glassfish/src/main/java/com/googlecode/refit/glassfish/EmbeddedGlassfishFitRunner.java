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
package com.googlecode.refit.glassfish;

import java.net.URI;

import com.googlecode.jeeunit.ContainerLauncherLookup;
import com.googlecode.jeeunit.spi.ContainerLauncher;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class EmbeddedGlassfishFitRunner implements Runnable {

    @Override
    public void run() {
        System.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties");
        ContainerLauncher launcher = ContainerLauncherLookup.getContainerLauncher();
        launcher.launch();
        launcher.autodeploy();

        Client client = Client.create();
        URI uri = launcher.getContextRootUri().resolve("fitrunner");
        WebResource testRunner = client.resource(uri);

        try {
            String result = testRunner //
                    .queryParam("inputDir", "src/test/fit") //
                    .queryParam("outputDir", "target/fit").get(String.class);
            System.out.println("Result from FitRunnerServlet: " + result);
        }
        catch (UniformInterfaceException exc) {
            exc.printStackTrace();
        }
        launcher.shutdown();
    }

    public static void main(String[] args) {
        new EmbeddedGlassfishFitRunner().run();
    }
}
