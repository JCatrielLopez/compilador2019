import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;


public final class TablaSimbolos {

    private static Hashtable<String, Integer> identificadores = new Hashtable<String,Integer>();
    private static Hashtable<String,Token> simbolos = new Hashtable<String,Token>();

    public TablaSimbolos(){
        identificadores.put("(", 40);
        identificadores.put(")", 41);
        identificadores.put("*", 42);
        identificadores.put("+", 43);
        identificadores.put(",", 44);
        identificadores.put("-", 45);
        identificadores.put("/", 47);
        identificadores.put(":", 58);
        identificadores.put(";", 59);
        identificadores.put("<", 60);
        identificadores.put("=", 61);
        identificadores.put(">", 62);
        identificadores.put("[", 91);
        identificadores.put("]", 93);
        identificadores.put("if", 257);
        identificadores.put("else", 258);
        identificadores.put("end_if", 259);
        identificadores.put("print", 260);
        identificadores.put("int", 261);
        identificadores.put("begin", 262);
        identificadores.put("end", 263);
        identificadores.put("ulong", 264);
        identificadores.put("while", 265);
        identificadores.put("cte", 270);
        identificadores.put("id", 268);
        identificadores.put("cadena", 269);
        identificadores.put("do", 266);
        identificadores.put(">=", 272);
        identificadores.put("<=", 273);
        identificadores.put("<>", 274);
        identificadores.put(":=", 275);
    }

    public static boolean contains(String id) {
        return (identificadores.containsKey(id));
    }

    public static int getID(String id) {
        return (identificadores.get(id));
    }

    public static void addSimbolo(Token t) {
        if (!simbolos.containsKey(t.getLexema()))
            simbolos.put(t.getLexema(),t);
    }

    public static Token getSimbolo(String lexema) {
        return simbolos.get(lexema);
    }

    public static Set<String> getLexemas() {
        return simbolos.keySet();
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        for (String s: simbolos.keySet()) {
            Token t = getSimbolo(s);
            out.append(t + "\n");
        }
        return out.toString();
    }

    public static boolean contiene(String lexema) {
        return simbolos.containsKey(lexema);
    }

    public static void remove(String lexema) {
        simbolos.remove(lexema);
    }

    public static void modificarLexema(String viejo, String nuevo) {
        Token t = getSimbolo(viejo);
        if (t != null) {
            t.setLexema(nuevo);
            remove(viejo);
            addSimbolo(t);
        }
    }

//    public static Set<String> iterator() {
//        return new HashSet<String>(simbolos.keySet());
//    }

}
