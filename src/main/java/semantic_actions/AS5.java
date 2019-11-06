package semantic_actions;

import globals.Error;
import lexer.Buffer;
import lexer.Token;

public class AS5 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {


        if ((int) last_char != 0) {
            Error.add(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "ERROR Caracter invalido: " + last_char));
        } else
            Error.add(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "ERROR Caracter invalido: EOF"));
        lex.setLength(0);

        return null;
    }
}