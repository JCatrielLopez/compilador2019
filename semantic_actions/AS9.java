public class AS9 implements SemanticAction {

    // Salto de linea
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.nextLine();
        return null;
    }
}
