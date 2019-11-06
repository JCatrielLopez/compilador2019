package globals;
import java.util.ArrayList;

public class Error {

    private static ArrayList<String> errores = new ArrayList<>();

    public static void add(String msg) {
        errores.add(msg);
    }

    public static void print() {
        for (String msg : errores) {
            Printer.print(msg);
        }
    }

    public static void remove(String msg) {
        errores.remove(msg);
    }
}
