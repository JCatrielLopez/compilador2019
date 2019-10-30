package semantic_actions;

import globals.Color;
import globals.Printer;
import lexer.Buffer;
import lexer.Token;

public class AS8 implements SemanticAction {

    // End of file
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        if ((last_char == 0))
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "EOF"), Color.BLUE);

        lex.setLength(0);

        return null;
    }
}
