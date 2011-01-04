package fit;

class CommandLineParseException extends CommandLineException {
    private static final long serialVersionUID = 1L;

    public CommandLineParseException(String message) {
        super("fit: " + message);
    }
}
