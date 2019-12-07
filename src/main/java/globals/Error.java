package globals;
import java.util.ArrayList;

public class Error {

    private static ArrayList<String> errores = new ArrayList<>();
    private static boolean error = false;

    public static void add(String msg) {
        errores.add(msg);
        if (msg.contains("ERROR"))
            error = true;
    }

    public static void print() {
        for (String msg : errores) {
            Printer.print(msg);
        }
    }

    public static boolean containsError(){
        return error;
    }
}
