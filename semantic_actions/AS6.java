public class AS6 implements SemanticAction {

    // Literales
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        source.returnChar(last_char);
//        System.out.println(lex.toString());

        return new Token(SymbolTable.getID(lex.toString().toLowerCase()), lex.toString().toLowerCase(), "LITERAL");
    }
}
