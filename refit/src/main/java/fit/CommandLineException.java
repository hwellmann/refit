package fit;

//Copyright (c) 2008 Cunningham & Cunningham, Inc.
//Released under the terms of the GNU General Public License version 2 or later.
//Contributed by James Shore with inspiration from Martin Busik

public class CommandLineException extends Exception {
	public CommandLineException(String message) { super(message); }
}

class CommandLineParseException extends CommandLineException {
	public CommandLineParseException(String message) { super("fit: " + message); }
}