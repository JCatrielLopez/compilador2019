public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {


//        source.returnChar(last_char);

        if ((int) last_char != 0)
            Printer.print("Line " + source.getLineNumber() + ": (AL) ERROR Invalid character: ", Color.RED);
        else
            Printer.print("Line " + source.getLineNumber() + ": (AL) ERROR Invalid character: EOF", Color.RED);
        lex.setLength(0);

        return null;
    }
}
