package lexer;

import java.util.Hashtable;
import java.util.Objects;

public class Token {

    private final int id;
    private String lex;
    private final String description;
    private final Hashtable<String, Object> attributes = new Hashtable<>();

    public Token(int id, String lex, String description) {
        this.id = id;
        this.lex = lex;
        this.description = description;
    }

    public void addAttr(String character, Object val) {
        attributes.put(character, val);
    }

    public String getAttr(String character) {
        return (String) attributes.get(character);
    }

    public int getID() {
        return id;
    }

    public void setLex(String lex) {
        this.lex = lex;
    }

    public String getLex() {
        return lex;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return String.format("%5s %s %25s %s %-10s", "(" + id + ")", " ", lex, " ", "[" + description + "]");
    }

    public Object getAttrs() {
        return this.attributes.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return id == token.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
