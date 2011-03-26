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

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.io.IOException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.QueryParameter;

@Extension
public final class DescriptorImpl extends BuildStepDescriptor<Publisher> {
    public DescriptorImpl() {
        super(ReFitArchiver.class);
    }

    /**
     * This human readable name is used in the configuration screen.
     */
    public String getDisplayName() {
        return "Publish Fit Test Reports";
    }

    /**
     * Performs on-the-fly validation on the file mask wildcard.
     */
    public FormValidation doCheck(@AncestorInPath AbstractProject<?, ?> project,
            @QueryParameter String value) throws IOException, ServletException {
        FilePath ws = project.getSomeWorkspace();
        return ws != null ? ws.validateRelativeDirectory(value) : FormValidation.ok();
    }

    @SuppressWarnings("rawtypes")
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        // this option should be available whether the job is Maven or not
        return true;
    }

}