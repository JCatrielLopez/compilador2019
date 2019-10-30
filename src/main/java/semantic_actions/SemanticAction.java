package semantic_actions;

import lexer.Buffer;
import lexer.Token;

public interface SemanticAction {
    Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose);
}
