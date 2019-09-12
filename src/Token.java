import java.util.Hashtable;

public class Token {

    private int id;
    private String lex, description;
    private Hashtable<String, Object> attributes = new Hashtable<String, Object>();

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

//    public void setLex(String lex) {
//        this.lex = lex;
//    }

    public String getLex() {
        return lex;
    }

//    public String getDescription() {
//        return description;
//    }

    public String toString() {
        return ("Token: " + id + "  | Lexeme: " + lex + "  | Description: " + description);
    }
}
