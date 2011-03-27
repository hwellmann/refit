package com.googlecode.refit.jenkins;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.googlecode.refit.jenkins.jaxb.ObjectFactory;
import com.googlecode.refit.jenkins.jaxb.Summary;

public class ReportReader {
    
    public static String FIT_REPORT_XML = "fit-report.xml";

    @SuppressWarnings("unchecked")
    public Summary readXml(InputStream is) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        JAXBElement<Summary> summaryRoot = (JAXBElement<Summary>) unmarshaller.unmarshal(is);
        Summary summary = summaryRoot.getValue();
        return summary;        
    }
}
