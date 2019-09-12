public class AS1 implements SemanticAction {
    @Override

    // Agregar caracter al lexema.
    public Token execute(Buffer source, StringBuilder lex, char last_char) {
        lex.append(last_char);

        return null;
    }
}
