public class AS10 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        source.returnChar(last_char);
        Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                "ERROR Caracter invalido: " + lex), Color.RED);
        lex.setLength(0);

        return null;
    }
}