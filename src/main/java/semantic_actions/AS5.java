package semantic_actions;

import globals.Color;
import globals.Printer;
import lexer.Buffer;
import lexer.Token;

public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {


        if ((int) last_char != 0) {
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "ERROR Caracter invalido: " + last_char), Color.RED);
        } else
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "ERROR Caracter invalido: EOF"), Color.RED);
        lex.setLength(0);

        return null;
    }
}