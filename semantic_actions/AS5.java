public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        source.returnChar(last_char);
        System.out.printf("LINE " + source.getLineNumber() + "(AL) ERROR Invalid character: " + lex);
        lex.setLength(0);

        return null;
    }
}
