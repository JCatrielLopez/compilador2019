public class AS2 implements SemanticAction {
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.returnChar(last_char);

        // Se trunca el identificador a un maximo de 25 caracteres.
        if (lex.length() > 25) {
            lex = new StringBuilder(lex.substring(0, 25));
        }

        Token token;

        //TODO No deberiamos preguntar si lo contiene la Tabla de Simbolos???
        if (!SymbolTable.isReserved(lex.toString())) {
            token = new Token(SymbolTable.getID("id"), lex.toString(), "ID");
            SymbolTable.add(token);
        } else {
            token = new Token(SymbolTable.getID(lex.toString()), lex.toString(), "RESERVED KEYWORD");
        }

        return token;

    }
}
