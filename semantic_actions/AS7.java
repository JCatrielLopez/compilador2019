public class AS7 implements SemanticAction {

    // Cadena de una linea
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        Token token = new Token(SymbolTable.getID("cadena"), lex.toString(), "IDENTIFIER");

        SymbolTable.add(token);

        return token;

    }
}
