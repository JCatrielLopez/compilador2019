public interface SemanticAction {
    Token execute(Buffer source, StringBuilder lex, char last_char);
}
