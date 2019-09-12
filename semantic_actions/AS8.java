public class AS8 implements SemanticAction {
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        String COLOR = "\u001B[32m";
        if (last_char == 0)
            System.out.println(COLOR + "END OF FILE" + COLOR);

        lex.setLength(0);

        return null;
    }
}
