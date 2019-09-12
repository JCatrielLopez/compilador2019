public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        String COLOR = "\u001B[31m";

//        source.returnChar(last_char);

        if ((int) last_char != 0)
            System.out.println(COLOR + "Line " + source.getLineNumber() + ": (AL) ERROR Invalid character: " + last_char + COLOR);
        else
            System.out.println(COLOR + "Line " + source.getLineNumber() + ": (AL) ERROR Invalid character: EOF" + COLOR);
        lex.setLength(0);

        return null;
    }
}
