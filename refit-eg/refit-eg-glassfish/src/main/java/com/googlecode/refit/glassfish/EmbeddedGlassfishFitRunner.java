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
