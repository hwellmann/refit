/*
 * Copyright 2010 Harald Wellmann
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.googlecode.refit.cdi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.ant.DirectoryScanner;

import fit.Fixture;
import fit.FixtureLoader;
import fit.Parse;

@WebServlet(urlPatterns = "/fitrunner")
public class FitRunnerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static {
        FixtureLoader.setInstance(new CdiFixtureLoader());
    }
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        String inputDir = request.getParameter("inputDir");
        String outputDir = request.getParameter("outputDir");
        String[] includes = request.getParameterValues("include");
        String[] excludes = request.getParameterValues("exclude");
        
        
        if (includes == null) {
            includes = new String[] { "**/*.html" };
        }
        
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(inputDir);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        assert files != null;
        if (files.length == 0) {
            throw new ServletException("no matching input files");
        }
        
        System.setProperty("fit.inputDir", inputDir);
        for (String testPath : files) {
            File inputFile = new File(inputDir, testPath);
            File outputFile = new File(outputDir, testPath);
            System.setProperty("fit.currentTest", testPath);
            try {
                ensureParentDirExists(outputFile);
                run(inputFile, outputFile);
            }
            catch (IOException exc) {
                throw new ServletException(exc);
            }
            catch (ParseException exc) {
                throw new ServletException(exc);
            }
        }
        
        
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("ok");
        writer.flush();
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
    
    private void run(File in, File out) throws IOException, ParseException {
        run(new FileReader(in), new FileWriter(out));
    }

    private void run(Reader reader, Writer writer) throws IOException, ParseException {
        String input = read(reader);
        Parse tables = new Parse(input);
        Fixture fixture = new Fixture();
        fixture.doTables(tables);
        PrintWriter output = new PrintWriter(writer);
        tables.print(output);
        output.flush();
    }

    /**
     * Returns the whole stream as a String.
     * @param in
     * @return
     * @throws IOException
     */
    private String read(Reader in) throws IOException {
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
}
