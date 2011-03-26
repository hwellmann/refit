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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.io.IOUtils;

public class EditPage extends WebPage {
    
    private String inputFileName = "/home/hwellmann/tmp/source.html";
    private String htmlText;
    
    @SuppressWarnings("serial")
    private class EditForm extends Form<String> {

        public EditForm(String id, IModel<String> model) {
            super(id, model);
            add(new TextArea<String>("editable", model));
        }
        
        @Override
        protected void onSubmit() {
            String editedText = getModelObject();
            saveText(editedText);
        }

    }

    
    public EditPage() {
        loadText();
        EditForm form = new EditForm("form", new Model<String>(htmlText));
        add(form);
    }


    private void loadText() {
        try {
            htmlText = IOUtils.toString(new FileInputStream(inputFileName), "UTF-8");
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    private void saveText(String editedText) {
        try {
            PrintWriter pw = new PrintWriter(inputFileName, "UTF-8");
            IOUtils.write(editedText, pw);
            pw.close();
        }
        catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
