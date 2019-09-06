public class AS8 implements SemanticAction {
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char) {

        if (last_char == 0)
            System.out.printf("END OF FILE.");

        return null;
    }
}
