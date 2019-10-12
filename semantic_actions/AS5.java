public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        lex.append(last_char);
        if (verbose) {
            if (lex.length() > 1){
                Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                            "ERROR Invalid character after: " + lex, "|", " "), Color.RED);
            }else{
                if ((int) last_char != 0) {
                    Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                            "ERROR Invalid character: " + lex, "|", " "), Color.RED);
                } else
                    Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                            "ERROR Invalid character: EOF", "|", " "), Color.RED);
            }

        }
        lex.setLength(0);

        return null;
    }
}
