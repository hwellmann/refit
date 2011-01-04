package fat;

//Copyright (c) 2008 Cunningham & Cunningham, Inc.
//Released under the terms of the GNU General Public License version 2 or later.
//Contributed by James Shore with inspiration from Martin Busik

import fit.*;

public class CommandLineFixture extends ColumnFixture {
	public String CommandLine;
	
	public String InputFile() throws Exception {
		return CommandLine().input();
	}

	public String OutputFile() throws Exception {
		return CommandLine().output();
	}

	public String Encoding() throws Exception {
		Parameters p = CommandLine();
		if (p.encodingSpecified()) return p.encoding();
		else return "Implementation-specific";
	}
	
	public Parameters CommandLine() throws Exception {
		String[] args = CommandLine.split(" ");
		return new Parameters(args);
	}
}