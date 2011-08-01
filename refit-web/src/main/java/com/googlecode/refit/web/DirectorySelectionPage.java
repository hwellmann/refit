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
package com.googlecode.refit.web;

import java.io.File;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.googlecode.refit.runner.ReportGenerator;
import com.googlecode.refit.runner.TreeRunner;
import com.googlecode.refit.runner.jaxb.Summary;

public class DirectorySelectionPage extends WebPage {
    
    private static final long serialVersionUID = 1L;

    private DirectorySelection selection;


    private class DirectorySelectionForm extends Form<DirectorySelection>{
        
        private static final long serialVersionUID = 1L;
        private Summary summary;

        public DirectorySelectionForm(String id, IModel<DirectorySelection> model) {
            super(id, model);
            
            add(new TextField<String>("inputDir"));
            add(new TextField<String>("outputDir"));
            add(new TextField<String>("includes"));
            add(new TextField<String>("excludes"));
        }
        
        @Override
        protected void onSubmit() {
            System.out.println("form submitted: " + selection);
            buildTreeRunner();
            
            setResponsePage(new SummaryPage(summary));
        }

        private void buildTreeRunner() {
            File inputDir = new File(selection.getInputDir());
            File outputDir = new File(selection.getOutputDir());
            ReportGenerator reportGenerator = new ReportGenerator(inputDir, outputDir);
            TreeRunner runner = new TreeRunner(inputDir, outputDir, reportGenerator);
            runner.run();
            summary = reportGenerator.getSummary();            
        }
    }

    
    public DirectorySelectionPage() {
        selection = new DirectorySelection();
        CompoundPropertyModel<DirectorySelection> model = new CompoundPropertyModel<DirectorySelection>(selection);
        DirectorySelectionForm form = new DirectorySelectionForm("form", model);
        add(form);
    }
}
