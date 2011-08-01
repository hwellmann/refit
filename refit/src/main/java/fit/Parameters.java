/*
 * Copyright 2011 Harald Wellmann
 * Copyright (c) 2002, 2008 Cunningham & Cunningham, Inc.
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
package fit;

public class Parameters {

	static final String HELP_TEXT = 
		"usage: java fit.FileRunner [options] input-file output-file\n" +
		"\n" +
		"Options\n" +
		" --encoding=CHARSET     read and write files using specified character set\n"+
		" --version              print version number\n" + 
		" --help                 show this help screen\n" +
		"\n" +
		"See http://refit.googlecode.com for further information and to report bugs\n"
	;
	
	static final String VERSION_TEXT =
		"reFIT for Java 1.5.0\n" +
		"Conforms to Fit Specification v1.1+changes\n" +
		"Copyright (C) 2011 Harald Wellmann\n" + 
		"Copyright (C) 2008 Cunningham & Cunningham, Inc.\n" + 
		"License GPLv2+: GNU GPL version 2 or later\n" +
		"This is free software: you are free to change and redistribute it.\n" +
		"There is NO WARRANTY, to the extent permitted by law.\n"
	;
	
	private boolean shouldParseOptions = true;
	private String input = null;
	private String output = null;
	private String encoding = null;

	public String input() { return input; }
	public String output() { return output; }
	public String encoding() { return encodingSpecified() ? encoding : System.getProperty("file.encoding"); }
	public boolean encodingSpecified() { return encoding != null; }
	public String[] legacyArguments() { return new String[] {input(), output()}; }

	public Parameters(String[] args) throws CommandLineException {
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if (arg.equals("--")) shouldParseOptions = false;
			else if (arg.startsWith("-") && shouldParseOptions) parseOption(arg);
			else if (input == null) input = arg;
			else if (output == null) output = arg;
			else throw new CommandLineParseException("too many file parameters");
		}
		
		if (input == null) throw new CommandLineParseException("missing input-file and output-file");
		if (output == null) throw new CommandLineParseException("missing output-file");
	}
	
	private void parseOption(String encodingParm) throws CommandLineException {
		String name = encodingParm;
		String value = null;
		int equalsAt = encodingParm.indexOf('=');
		if (equalsAt >= 0) {
			name = encodingParm.substring(0, equalsAt);
			value = encodingParm.substring(equalsAt + 1);
		}

		if (name.equals("--encoding")) parseEncoding(value);
		else if (name.equals("--version")) throw new CommandLineException(VERSION_TEXT);
		else if (name.equals("--help")) throw new CommandLineException(HELP_TEXT);
		else throw new CommandLineParseException(name + ": unknown option");
	}
	
	private void parseEncoding(String value) throws CommandLineException {
		if (value == null || "".equals(value)) throw new CommandLineParseException("--encoding: missing value");
		if (encodingSpecified()) throw new CommandLineParseException("--encoding: duplicated parameter");

		encoding = value;
	}
}
