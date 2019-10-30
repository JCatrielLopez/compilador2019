package parser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

public class AdminTercetos {

    public static LinkedHashMap<String, Terceto> tercetos = new LinkedHashMap<>();
    public static Stack<String> pila = new Stack<>();


    public static Terceto getTerceto(String id) {
        return tercetos.get(id);
    }

    public static void addTerceto(Terceto t) {
        tercetos.put(t.getId(), t);
    }

    public static void push(String id) {
        pila.push(id);
    }

    public static String pop() {
        if (!pila.isEmpty())
            return pila.pop();
        return null;
    }

    public static int cantTercetos() {
        return tercetos.size();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Terceto> entry : tercetos.entrySet())
            out.append(entry.getValue() + "\n"); // entry: (k, V)

        return out.toString();
    }
}
