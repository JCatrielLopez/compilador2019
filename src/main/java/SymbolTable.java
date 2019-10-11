import java.util.Hashtable;


final class SymbolTable {

    private static final Hashtable<String, Integer> ids = new Hashtable<>();
    private static final Hashtable<String, Token> symbols = new Hashtable<>();

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
        ids.put("if", 257);
        ids.put("else", 258);
        ids.put("end_if", 259);
        ids.put(":=", 271);
        ids.put("print", 260);
        ids.put("cte", 267);
        ids.put("int", 261);
        ids.put("ulong", 262);
        ids.put("first", 274);
        ids.put("last", 275);
        ids.put("length", 276);
        ids.put("begin", 272);
        ids.put("end", 273);
        ids.put("while", 263);
        ids.put("do", 264);
        ids.put("id", 265);
        ids.put("cadena", 266);
        ids.put(">=", 268);
        ids.put("<=", 269);
        ids.put("<>", 270);
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
        System.out.println("=====================================================================");
        System.out.println("                           SYMBOL TABLE                              ");
        System.out.println("=====================================================================");
        System.out.println(String.format("%5s %s %8s %10s %10s %s %10s %s %-45s", "Token",
                "|", " ", "Lexeme", " ", "|", "Descr", "|", "Otros atributos"));
        System.out.println("---------------------------------------------------------------------");

        for (String entry : symbols.keySet()) {
            Printer.print(String.format("%s %s %s", symbols.get(entry).toString(), "|", symbols.get(entry).getAttrs()), Color.RESET);
        }
    }

    public static boolean contains(String id) {
        return (ids.containsKey(id));
    }

    public static void modify(String old, String new_lex) {
        Token t = getLex(old);
        if (t != null) {
            t.setLex(new_lex);
            remove(old);
            add(t);
        }
    }

//    public static Set<String> iterator() {
//        return new HashSet<String>(simbolos.keySet());
//    }

}
