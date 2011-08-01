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
package com.googlecode.refit.cdi;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.refit.runner.TreeRunner;

import fit.FixtureLoader;

@WebServlet(urlPatterns = "/fitrunner")
public class FitRunnerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    static {
        FixtureLoader.setInstance(new CdiFixtureLoader());
    }
    
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        File inputDir = new File(request.getParameter("inputDir"));
        File outputDir = new File(request.getParameter("outputDir"));
        String[] includes = request.getParameterValues("include");
        String[] excludes = request.getParameterValues("exclude");

        if (includes == null) {
            includes = new String[] { "**/*.html" };
        }

        TreeRunner runner = new TreeRunner(inputDir, outputDir, includes, excludes);
        
        try {
            runner.run();
        }
        catch (Exception exc) {
            throw new ServletException(exc);
        }
        
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("ok");
        writer.flush();
    }
}
