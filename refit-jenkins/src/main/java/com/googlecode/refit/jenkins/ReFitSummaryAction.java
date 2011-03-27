/*
 * The MIT License
 * 
 * Copyright (c) 2011, Harald Wellmann
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.refit.jenkins;

import hudson.FilePath;
import hudson.model.AbstractItem;
import hudson.model.DirectoryBrowserSupport;
import hudson.model.ProminentProjectAction;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public final class ReFitSummaryAction implements ProminentProjectAction {
    private static final String ICON_URL = "/plugin/refit-jenkins/img/reFitLogo.png";
    private static final long serialVersionUID = 4399590075673857468L;
    private final AbstractItem project;

    public ReFitSummaryAction(AbstractItem project) {
        this.project = project;
    }

    public String getUrlName() {
        return "fit";
    }

    public String getDisplayName() {
        return "Fit Test Report";
    }

    public String getIconFileName() {
        if (ReFitArchiver.getTargetDir(project).exists()) {
            return ICON_URL;
        }
        else {
            return null;
        }
    }

    public DirectoryBrowserSupport doDynamic(StaplerRequest req, StaplerResponse rsp)
            throws IOException, ServletException, InterruptedException {

        String title = project.getDisplayName();
        FilePath systemDirectory = new FilePath(ReFitArchiver.getTargetDir(project));
        return new DirectoryBrowserSupport(this, systemDirectory, title, ICON_URL, false);
    }
}