public class AS8 implements SemanticAction {

    // End of file
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        if ((last_char == 0) && verbose)
            Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                    "END OF FILE", "|", " "), Color.YELLOW);

        lex.setLength(0);

        return null;
    }
}
