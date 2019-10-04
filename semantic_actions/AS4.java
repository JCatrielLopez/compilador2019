public class AS4 implements SemanticAction {

    // Agregar caracter y retornar token
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        lex.append(last_char);
//        System.out.println(lex.toString());

        return new Token(SymbolTable.getID(lex.toString()), lex.toString(), "LITERAL");
    }
}
