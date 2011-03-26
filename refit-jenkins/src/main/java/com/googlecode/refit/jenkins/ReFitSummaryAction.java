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