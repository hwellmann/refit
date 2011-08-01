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
package com.googlecode.refit.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.googlecode.refit.runner.jaxb.Summary;
import com.googlecode.refit.runner.jaxb.TestResult;

public class TreeRunnerTest {
    
    @Test
    public void runSuite() throws IOException, JAXBException {
        File inputDir = new File("src/test/fit");
        File outputDir = new File("target/fit");
        
        ReportGenerator reportGenerator = new ReportGenerator(inputDir, outputDir);
        TreeRunner runner = new TreeRunner(inputDir, outputDir, reportGenerator);
        boolean passed = runner.run();
        assertFalse(passed);
        
        reportGenerator.createReports();
        
        File xmlReport = new File(outputDir, ReportIO.FIT_REPORT_XML);
        checkXmlReport(xmlReport);
                
        File htmlReport = new File(outputDir, ReportIO.FIT_REPORT_HTML);
        assertTrue(htmlReport.exists());
        
        File htmlExpected = new File("src/test/resources/expected", ReportIO.FIT_REPORT_HTML);
        assertTrue(FileUtils.contentEquals(htmlExpected, htmlReport));

        File css = new File(outputDir, ReportIO.FIT_CSS);
        assertTrue(css.exists());
    }

    public void checkXmlReport(File xmlReport) throws JAXBException {
        assertTrue(xmlReport.exists());

        JAXBContext ctx = JAXBContext.newInstance(ReportIO.CONTEXT_PATH);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        
        @SuppressWarnings("unchecked")
        JAXBElement<Summary> root = (JAXBElement<Summary>) unmarshaller.unmarshal(xmlReport);
        
        Summary summary = root.getValue();
        assertTrue(summary.getInputDir().endsWith("src/test/fit"));
        assertTrue(summary.getOutputDir().endsWith("target/fit"));
        assertEquals(1, summary.getExceptions());
        assertEquals(0, summary.getIgnored());
        assertEquals(10, summary.getWrong());
        assertEquals(339, summary.getRight());        
        assertEquals(4, summary.getNumTests());
        
        List<TestResult> results = summary.getTest();
        checkResult(results.get(0), 0, 0, 0, 95, true, "MusicExample.html");
        checkResult(results.get(1), 1, 0, 0, 95, false, "MusicExampleWithErrors.html");
        checkResult(results.get(2), 0, 0, 10, 54, false, "MusicExampleWithFailures.html");
        checkResult(results.get(3), 0, 0, 0, 95, true, "subdir/MusicExample.html");
    }

    private void checkResult(TestResult testResult, int exceptions, int ignored, int wrong, int right, boolean passed,
            String path) {
        assertEquals(exceptions, testResult.getExceptions());
        assertEquals(ignored, testResult.getIgnored());
        assertEquals(wrong, testResult.getWrong());
        assertEquals(right, testResult.getRight());
        assertEquals(passed, testResult.isPassed());
        assertEquals(path, testResult.getPath());
    }
}
