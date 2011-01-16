package com.googlecode.refit.runner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class TreeRunnerTest {
    
    @Test
    public void runSuite() {
        File inputDir = new File("src/test/fit");
        File outputDir = new File("target/fit");
        
        TreeRunner runner = new TreeRunner(inputDir, outputDir);
        boolean passed = runner.run();
        assertFalse(passed);
        
        File xmlReport = new File(outputDir, ReportIO.FIT_REPORT_XML);
        assertTrue(xmlReport.exists());

        File htmlReport = new File(outputDir, ReportIO.FIT_REPORT_HTML);
        assertTrue(htmlReport.exists());

        File css = new File(outputDir, ReportIO.FIT_CSS);
        assertTrue(css.exists());
    }

}
