public class AS8 implements SemanticAction {
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        if (last_char == 0)
            Printer.print("END OF FILE", Color.YELLOW);

        lex.setLength(0);

        return null;
    }
}
