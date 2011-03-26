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

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.basic.URIRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.apache.wicket.util.resource.FileResourceStream;

public class ReFitApplication extends WebApplication {
    
    @Override
    protected void init() {
        super.init();
        
        mountBookmarkablePage("/edit", EditPage.class);
        
        mount(new URIRequestTargetUrlCodingStrategy("/static") {
            
            @Override
            public IRequestTarget decode(RequestParameters requestParameters) {
                String uri = getURI(requestParameters);
                System.out.println("uri = " + uri);
                FileResourceStream stream = new FileResourceStream(new File("/" + uri));
                return new ResourceStreamRequestTarget(stream);
            }
            
        });
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return DirectorySelectionPage.class;
    }

}
