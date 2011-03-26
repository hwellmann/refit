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


import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.googlecode.refit.runner.jaxb.Summary;
import com.googlecode.refit.runner.jaxb.TestResult;

public class SummaryPage extends WebPage {
    
    public SummaryPage(final Summary summary) {
        setDefaultModel(new CompoundPropertyModel<Summary>(summary));
        
        add(new Label("inputDir"));
        add(new Label("outputDir"));
        add(new Label("passed"));
        add(new Label("numTests"));
        
        add(new ListView<TestResult>("test", summary.getTest()) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<TestResult> item) {
                TestResult testResult = item.getModelObject();
                item.setModel(new CompoundPropertyModel<TestResult>(testResult));
                String uri = String.format("static%s/%s", summary.getInputDir(), testResult.getPath());
                StaticLink link = new StaticLink("link", new Model<String>(uri));
                item.add(link);
                link.add(new Label("path"));
                item.add(new Label("right"));
                item.add(new Label("wrong"));
                item.add(new Label("ignored"));
                item.add(new Label("exceptions"));                
            }
            
        });        
    }

    private static class StaticLink extends WebMarkupContainer
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public StaticLink(String id, IModel<String> model)
        {
            super(id, model);
            add(new AttributeModifier("href", true, model));
        }
    }
    
}
