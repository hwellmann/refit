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

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.googlecode.refit.runner.jaxb.Summary;
import com.googlecode.refit.runner.jaxb.TestResult;

public class ReportGenerator implements RunnerListener {
    
    private Summary summary;
    private int totalRight;
    private int totalWrong;
    private int totalIgnored;
    private int totalExceptions;
    private File outputDir;

    public ReportGenerator(File inputDir, File outputDir) {        
        this.summary = new Summary();
        this.outputDir = outputDir;
        summary.setInputDir(inputDir.getPath());
        summary.setOutputDir(outputDir.getPath());
    }

    @Override
    public void beforeTest(String testPath) {
    }

    @Override
    public void afterTest(TestResult result) {
        summary.getTest().add(result);
        
        totalRight += result.getRight();
        totalWrong += result.getWrong();
        totalIgnored += result.getIgnored();
        totalExceptions += result.getExceptions();
    }

    public void createReports() {
        buildSummary();
        printSummary();
    }
    
    public Summary getSummary() {
        return summary;
    }
    
    private void buildSummary() {
        summary.setNumTests(summary.getTest().size());
        
        boolean success = (totalExceptions == 0) && (totalWrong == 0);
        summary.setPassed(success);
        summary.setRight(totalRight);
        summary.setWrong(totalWrong);
        summary.setIgnored(totalIgnored);
        summary.setExceptions(totalExceptions);
    }
    
    private void printSummary() {
        ReportIO writer = new ReportIO(summary);
        File xmlReport = new File(outputDir, ReportIO.FIT_REPORT_XML);        
        File htmlReport = new File(outputDir, ReportIO.FIT_REPORT_HTML);        
        File css = new File(outputDir, "fit.css");        
        try {
            writer.writeXml(xmlReport);
            writer.writeHtml(htmlReport);
            writer.writeCss(css);
        }
        catch (JAXBException exc) {
            throw new RuntimeException(exc);
        }              
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    
}
