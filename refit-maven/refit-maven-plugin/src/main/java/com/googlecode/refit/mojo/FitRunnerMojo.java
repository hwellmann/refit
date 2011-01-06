package com.googlecode.refit.mojo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.DirectoryScanner;

import fit.Fixture;
import fit.Parse;
import static java.lang.Thread.currentThread;

/**
 * Mojo to run Fit tests via a Fixture
 * 
 * @author Mauro Talevi, Harald Wellmann
 * @goal run
 * @phase integration-test
 * @requiresDependencyResolution test
 */
public class FitRunnerMojo extends AbstractMojo {

    private static final String COMMA = ",";

    private static final String WIKI_TAG = "wiki";

    private static final String EXECUTION_PARAMETERS = "sourceDirectory={0}, caseSensitive={1},"
            + " sourceIncludes={2}, sourceExcludes={3}, parseTags={4}, outputDirectory={5}, ignoreFailures={6}";

    /**
     * Classpath.
     * 
     * @parameter expression="${project.testClasspathElements}"
     * @required
     */
    protected List<String> classpathElements;

    /**
     * The source directory containing the Fit fixtures
     * 
     * @parameter
     * @required
     */
    protected String sourceDirectory;

    /**
     * Flag to indicate that path names are case sensitive
     * 
     * @parameter default-value="false"
     */
    protected boolean caseSensitive;

    /**
     * The filter for source file includes, relative to the source directory, as CSV patterns.
     * 
     * @parameter
     */
    protected String sourceIncludes;

    /**
     * The filter for source file excludes, relative to the source directory, as CSV patterns.
     * 
     * @parameter
     */
    protected String sourceExcludes;

    /**
     * The parsee tags used to identify the Fit tables.
     * 
     * @parameter
     */
    protected String[] parseTags = new String[] { "table", "tr", "td" };

    /**
     * The output directory where the results of Fit processing is written to
     * 
     * @parameter
     * @required
     */
    protected String outputDirectory;

    /**
     * The option to ignore fixture failures
     * 
     * @parameter default-value="false"
     */
    protected boolean ignoreFailures;

    /**
     * The scanner to list files
     */
    private DirectoryScanner scanner = new DirectoryScanner();

    private ClasspathClassLoader testClassLoader;

    public void execute() throws MojoExecutionException, MojoFailureException {
        final String executionParameters = MessageFormat.format(EXECUTION_PARAMETERS, new Object[] {
                sourceDirectory, Boolean.valueOf(caseSensitive), sourceIncludes, sourceExcludes,
                Arrays.asList(parseTags), outputDirectory, Boolean.valueOf(ignoreFailures) });
        getLog().debug("Executing FitRunner with parameters " + executionParameters);
        System.setProperty("fit.inputDir", sourceDirectory);
        
        /*
         * The test classpath of the project using this plugin is not visible to the classloader
         * of this plugin, but it may be needed to loaded classes and resources for fixtures.
         * So we build our own class loader for the test classpath and temporarily install it
         * as context class loader.
         */
        
        ClassLoader ccl = currentThread().getContextClassLoader();
        try {
            currentThread().setContextClassLoader(getTestClassLoader());
            run(sourceDirectory, caseSensitive, sourceIncludes, sourceExcludes, outputDirectory);
        }
        catch (Exception e) {
            throw new MojoExecutionException("Failed to execute FitRunner with parameters "
                    + executionParameters, e);
        }
        finally {
            currentThread().setContextClassLoader(ccl);
        }
    }

    protected Fixture createFixture() {
        Fixture fixture = new Fixture();
        return fixture;
    }

    protected ClasspathClassLoader getTestClassLoader() {
        if (testClassLoader == null) {
            try {
                testClassLoader = new ClasspathClassLoader(classpathElements);
            }
            catch (MalformedURLException exc) {
                throw new IllegalStateException("error in classpath", exc);
            }
            getLog().debug("Created test path classloader with classpathElements " + classpathElements);
        }
        return testClassLoader;
    }

    protected String toPath(String directory, String name) {
        return new File(directory, name).getPath();
    }

    protected String[] listFiles(String sourceDirectory, boolean caseSensitive,
            String sourceIncludes, String sourceExcludes) {
        scanner.setBasedir(new File(sourceDirectory));
        getLog().debug("Listing files from directory " + sourceDirectory);
        getLog().debug("Setting case sensitive " + caseSensitive);
        scanner.setCaseSensitive(caseSensitive);
        if (sourceIncludes != null) {
            getLog().debug("Setting includes " + sourceIncludes);
            scanner.setIncludes(sourceIncludes.split(COMMA));
        }
        if (sourceExcludes != null) {
            getLog().debug("Setting excludes " + sourceExcludes);
            scanner.setExcludes(sourceExcludes.split(COMMA));
        }
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        getLog().debug("Files listed " + Arrays.asList(files));
        return files;
    }

    protected void ensureDirectoryExists(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            getLog().debug("Creating directory " + file);
            file.mkdirs();
        }
    }

    protected void run(String sourceDirectory, boolean caseSensitive, String sourceIncludes,
            String sourceExcludes, String outputDirectory) throws Exception {
        ensureDirectoryExists(outputDirectory);
        String[] files = listFiles(sourceDirectory, caseSensitive, sourceIncludes, sourceExcludes);
        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            System.setProperty("fit.currentTest", file);
			String inputPath = toPath(sourceDirectory, file);
            String outputPath = toPath(outputDirectory, file);
            run(inputPath, outputPath);
        }
    }

    protected void run(String in, String out) throws IOException, ParseException {
        run(new File(in), new File(out));
    }

    protected void run(File in, File out) throws IOException, ParseException {
        ensureParentDirExists(out);
        
        getLog().info(
                "Running Fixture with input file " + in.getPath() + " and output file "
                        + out.getPath());
        run(new FileReader(in), new FileWriter(out));
    }

    protected void run(Reader reader, Writer writer) throws IOException, ParseException {
        String input = read(reader);
        Parse tables = parse(input, parseTags);
        Fixture fixture = createFixture();
        fixture.doTables(tables);
        PrintWriter output = new PrintWriter(writer);
        tables.print(output);
        output.flush();
        if (failed(fixture)) {
            String message = "Fixture failed with counts: " + fixture.counts();
            if (ignoreFailures) {
                getLog().warn(message);
            }
            else {
                throw new IllegalStateException(message);
            }
        }
    }

    protected boolean failed(Fixture fixture) {
        if (fixture.counts.wrong > 0 || fixture.counts.exceptions > 0) {
            return true;
        }
        return false;
    }

    private Parse parse(String input, String[] parseTags) throws ParseException {
        Parse tables = new Parse(input, parseTags);
        if (parseTags.length > 0 && WIKI_TAG.equals(parseTags[0])) {
            getLog().debug("Found parse tag " + WIKI_TAG + ".  Processing contained tags.");
            return tables.parts;
        }
        return tables;
    }

    protected String read(Reader in) throws IOException {
        BufferedReader br = new BufferedReader(in);
        StringBuffer sb = new StringBuffer();
        String line = br.readLine();
        while (line != null) {
            sb.append(line);
            line = br.readLine();
        }
        in.close();
        return sb.toString();
    }
    
    private void ensureParentDirExists(File outputFile) throws IOException {
        File parentDir = outputFile.getParentFile();
        if (parentDir.exists()) {
            if (!parentDir.isDirectory()) {
                throw new IOException(parentDir + " is not a directory");
            }
        }
        else if (!parentDir.mkdirs()) {
            throw new IOException("cannot create " + parentDir);
        }
    }
    
    
}
