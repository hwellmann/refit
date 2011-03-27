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
public class ReFitArchiverDescriptor extends BuildStepDescriptor<Publisher> {
    public ReFitArchiverDescriptor() {
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