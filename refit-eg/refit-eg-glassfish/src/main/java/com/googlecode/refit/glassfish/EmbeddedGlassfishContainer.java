package com.googlecode.refit.glassfish;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.glassfish.embeddable.archive.ScatteredArchive.Type;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class EmbeddedGlassfishContainer {

    private static EmbeddedGlassfishContainer instance;

    private GlassFish glassFish;

    private String applicationName;
    private String contextRoot;
    private File configuration;

    private EmbeddedGlassfishContainer() {

        File domainConfig = new File("src/main/resources/domain.xml");
        setConfiguration(domainConfig);

        setApplicationName("refit");
        setContextRoot("/refit");
    }

    public static synchronized EmbeddedGlassfishContainer getInstance() {
        if (instance == null) {
            instance = new EmbeddedGlassfishContainer();
        }
        return instance;
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    protected String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the Java EE application name.
     * 
     * @param applicationName
     */
    protected void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    protected String getContextRoot() {
        return contextRoot;
    }

    /**
     * Sets the context root for the deployed test application.
     * 
     * @param contextRoot
     */
    protected void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    protected File getConfiguration() {
        return configuration;
    }

    /**
     * Sets the configuration file for the embedded server.
     * 
     * @param configuration
     */
    protected void setConfiguration(File configuration) {
        this.configuration = configuration;
    }

    public synchronized void launch() {
        if (glassFish != null) {
            return;
        }
        addShutdownHook();

        String classpath = System.getProperty("java.class.path");

        // Define your JDBC resources and JNDI names in this config file
        File domainConfig = getConfiguration();
        // assertTrue(domainConfig + " not found", domainConfig.exists());

        GlassFishProperties gfProps = new GlassFishProperties();
        gfProps.setConfigFileURI(domainConfig.toURI().toString());
        try {

            glassFish = GlassFishRuntime.bootstrap().newGlassFish(gfProps);
            glassFish.start();

            URI warUri = buildWar(classpath);
            deployWar(warUri);
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
        catch (GlassFishException exc) {
            throw new RuntimeException(exc);
        }
    }

    private URI buildWar(String classpath) throws IOException {
        ScatteredArchive sar = new ScatteredArchive("mywar", Type.WAR);
        String[] pathElems = classpath.split(File.pathSeparator);
        for (String pathElem : pathElems) {
            if (pathElem.contains("glassfish-embedded"))
                continue;

            if (pathElem.contains("org.eclipse.osgi"))
                continue;

            File file = new File(pathElem);
            if (file.exists()) {
                sar.addClassPath(file);
            }
        }
        File beansXml = new File("src/main/webapp/WEB-INF", "beans.xml");
        if (beansXml.exists()) {
            sar.addMetadata(beansXml);
        }
        URI warUri = sar.toURI();
        return warUri;
    }

    private void deployWar(URI warUri) throws GlassFishException {
        Deployer deployer = glassFish.getDeployer();
        deployer.deploy(warUri, "--name", getApplicationName(), "--contextroot", getContextRoot());
    }

    public URI getContextRootUri() {
        int port = getPortNumber(configuration);
        try {
            return new URI(String.format("http://localhost:%d/%s/", port, getContextRoot()));
        }
        catch (URISyntaxException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Reads the first port number from the domain.xml configuration.
     * 
     * @param domainConfig
     * @return
     */
    private int getPortNumber(File domainConfig) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(domainConfig);
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xPath = xpf.newXPath();
            String portPath = "/domain/configs/config/network-config/network-listeners/network-listener/@port";
            String port = xPath.evaluate(portPath, doc);
            return Integer.parseInt(port);
        }
        catch (ParserConfigurationException exc) {
            throw new IllegalArgumentException(exc);
        }
        catch (SAXException exc) {
            throw new IllegalArgumentException(exc);
        }
        catch (IOException exc) {
            throw new IllegalArgumentException(exc);
        }
        catch (XPathExpressionException exc) {
            throw new IllegalArgumentException(exc);
        }
    }

    public void shutdown() {
        try {
            if (glassFish != null) {
                glassFish.getDeployer().undeploy(getApplicationName());
                glassFish.stop();
                glassFish = null;
            }
        }
        catch (GlassFishException exc) {
            throw new RuntimeException(exc);
        }
    }

}
