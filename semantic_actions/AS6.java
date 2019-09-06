public class AS6 implements SemanticAction {

    // Literales
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        source.returnChar(last_char);
        System.out.println("Lexema: " + lex.toString());
        Token token = new Token(SymbolTable.getID(lex.toString()), lex.toString(), "LITERAL");

        return token;
    }
}
