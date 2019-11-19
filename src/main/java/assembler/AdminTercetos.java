package assembler;

import java.util.ArrayList;
import java.util.Stack;

public class AdminTercetos{

    private static ArrayList<Terceto> tercetos = new ArrayList<>();
    private static Stack<String> pila = new Stack<>();


    public static Terceto get(String id) {
        return tercetos.get(Integer.parseInt(id.substring(1,id.length()-1))-1);
    }

    public static Terceto last() {
        return tercetos.get(tercetos.size() - 1);
    }

    public static void add(Terceto t) {
        tercetos.add(t);
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

    public static String print() {

        StringBuilder out = new StringBuilder();
        for (Terceto t : tercetos) {
            out.append(t.toString())
               .append("\n");
        }

        return out.toString();
    }

    public static ArrayList<Terceto> list(){
        return tercetos;
    }
}
