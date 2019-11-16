package globals;

import lexer.Token;

import java.util.HashMap;
import java.util.Set;


public final class SymbolTable {

    private static final HashMap<String, Integer> ids = new HashMap<>();
    private static final HashMap<String, Token> symbols = new HashMap<>();

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
        System.out.println("\n--------------------------- TABLA DE SIMBOLOS ---------------------------------");
        System.out.println(
                String.format(
                        "%-5s %s %25s %s %-10s %s %15s", "  id", " ", "Lexema", " ", "Tipo", " ", "Otros atributos"));
        System.out.println("-------------------------------------------------------------------------------");

        for (String entry : symbols.keySet()) {
            Printer.print(String.format("%s %s %s", symbols.get(entry), " ",
                    symbols.get(entry).getAttrs()));
        }
    }

    public static boolean contains(String id) {
        return (ids.containsKey(id));
    }

    public static Set<String> keys() {
        return symbols.keySet();
    }

//    public static Set<String> iterator() {
//        return new HashSet<String>(simbolos.keySet());
//    }

}