/*
 * Copyright 2013 Harald Wellmann
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
package com.googlecode.refit.paxexam;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.junit.PaxExam;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

@RunWith(PaxExam.class)
public class PaxExamFitRunner {
    
    @Test
    public void runFitSuite() throws URISyntaxException {
        Client client = Client.create();
        URI uri = new URI("http://localhost:18080/Pax-Exam-Probe/fitrunner");
        WebResource testRunner = client.resource(uri);

        try {
            String result = testRunner //
                    .queryParam("inputDir", "src/test/fit") //
                    .queryParam("outputDir", "target/fit").get(String.class);
            System.out.println("Result from FitRunnerServlet: " + result);
        }
        catch (UniformInterfaceException exc) {
            exc.printStackTrace();
        }
        
    }

}
