package semantic_actions;

import globals.Error;
import lexer.Buffer;
import lexer.Token;

public class AS10 implements SemanticAction {

    // Caracter invalido
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {

        source.returnChar(last_char);
        Error.add(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                "ERROR Caracter invalido: " + lex));
        lex.setLength(0);

        return null;
    }
}