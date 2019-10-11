public class AS10 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        source.returnChar(last_char);

        if (verbose){
            Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                    "ERROR Invalid character: " + lex, "|", " "), Color.RED);
        }
        lex.setLength(0);

        return null;
    }
}