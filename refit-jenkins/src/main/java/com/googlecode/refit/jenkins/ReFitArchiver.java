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
package com.googlecode.refit.jenkins;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractItem;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;

import javax.xml.bind.JAXBException;

import org.kohsuke.stapler.DataBoundConstructor;

import com.googlecode.refit.runner.ReportIO;
import com.googlecode.refit.runner.jaxb.Summary;

@SuppressWarnings("unchecked")
public class ReFitArchiver extends Recorder {

    private String reportPath;

    @DataBoundConstructor
    public ReFitArchiver(String reportPath) {
        this.reportPath = reportPath;
    }

    public String getReportPath() {
        return reportPath;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        PrintStream logger = listener.getLogger();
        if (build.getResult().isWorseOrEqualTo(Result.FAILURE)) {
            logger.println("[reFit] not collecting results due to build failure");
            return true;
        }
        AbstractProject project = build.getProject();
        FilePath report = build.getWorkspace().child(reportPath);
        if (! report.exists()) {
            logger.println("[reFit] report directory " + report + " does not exist");
            build.setResult(Result.FAILURE);
            return true;
        }
        Summary summary = getSummary(report);
        int numTests = summary.getNumTests();
        logger.println("[reFit] found " + numTests + " Fit tests");
        if (! summary.isPassed()) {
            build.setResult(Result.UNSTABLE);
        }
        
        File targetDir = getTargetDir(project);
        FilePath archive = new FilePath(targetDir);
        archive.deleteContents();
        report.copyRecursiveTo(archive);

        return true;
    }

    public Summary getSummary(FilePath report) throws IOException {
        InputStream is = report.child(ReportIO.FIT_REPORT_XML).read();
        try {
            Summary summary = new ReportIO().readXml(is);
            return summary;
        }
        catch (JAXBException e) {
            throw new IOException(e);
        }
    }

    @Override
    public Collection<? extends Action> getProjectActions(AbstractProject<?, ?> project) {
        ReFitSummaryAction fitAction = new ReFitSummaryAction(project);
        return Collections.singleton(fitAction);
    }

    /**
     * Gets the directory where the files will be archived.
     */
    static File getTargetDir(AbstractItem project) {
        return new File(project.getRootDir(), "fit");
    }
}
