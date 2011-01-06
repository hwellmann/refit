package com.googlecode.refit.junit;

public class DefaultFitConfiguration {
	
	public static final String INPUT_DIR = "src/test/fit";
	public static final String OUTPUT_DIR = "target/fit";
	public static final String INCLUDE_HTML = "**/*.html";

	public String getInputDir() {
		return INPUT_DIR;
	}
    public String getOutputDir() {
    	return OUTPUT_DIR;
    }
    
    public String[] getIncludes() {
    	return new String[] { INCLUDE_HTML };
    }
    public String[] getExcludes() {
    	return new String[0];
    }
}
