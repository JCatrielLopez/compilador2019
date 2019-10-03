import java.util.Hashtable;


public final class SymbolTable {

    private static Hashtable<String, Integer> ids = new Hashtable<String, Integer>();
    private static Hashtable<String, Token> symbols = new Hashtable<String, Token>();

    public static void init() {
        ids.put("(", 40);
        ids.put(")", 41);
        ids.put("*", 42);
        ids.put("+", 43);
        ids.put(",", 44);
        ids.put("-", 45);
        ids.put(".", 46);
        ids.put("/", 47);
        ids.put(":", 58);
        ids.put(";", 59);
        ids.put("<", 60);
        ids.put(">", 62);
        ids.put("[", 91);
        ids.put("]", 93);
        ids.put(":=", 200);
        ids.put("if", 257);
        ids.put("else", 258);
        ids.put("end_if", 259);
        ids.put(":=", 200);
        ids.put("print", 260);
        ids.put("cte", 270);
        ids.put("int", 261);
        ids.put("ulong", 264);
        ids.put("first", 270);
        ids.put("last", 270);
        ids.put("length", 270);
        ids.put("begin", 262);
        ids.put("end", 263);
        ids.put("while", 265);
        ids.put("do", 266);
        ids.put("id", 268);
        ids.put("cadena", 269);
        ids.put(">=", 272);
        ids.put("<=", 273);
        ids.put("<>", 274);
        ids.put("==", 275);
    }


    public static int getID(String id) {
        return (ids.get(id));
    }

    public static void add(Token t) {
        if (!symbols.containsKey(t.getLex()))
            symbols.put(t.getLex(), t);
    }

    public static Token getLex(String lex) {
        return symbols.get(lex);
    }

//    public static Set<String> getLexemas() {
//        return symbols.keySet();
//    }

    public static boolean isReserved(String lex) {
        return ids.containsKey(lex);
    }

    public static void remove(String lex) {
        symbols.remove(lex);
    }

    public static void print() {

    }

//    public static boolean contains(String id) {
//        return (ids.containsKey(id));
//    }

//    public String toString() { //TODO Ver como imprimir de manera decente
//        StringBuffer out = new StringBuffer();
//        for (String s : symbols.keySet()) {
//            Token t = getLex(s);
//            out.append(t + "\n");
//        }
//        return out.toString();
//    }

//    public static void modificarLexema(String viejo, String nuevo) {
//        Token t = getLexeme(viejo);
//        if (t != null) {
//            t.setLexema(nuevo);
//            remove(viejo);
//            add(t);
//        }
//    }

//    public static Set<String> iterator() {
//        return new HashSet<String>(simbolos.keySet());
//    }

}
