package globals;

import static org.fusesource.jansi.Ansi.ansi;

public class Printer {
    static String TOKEN_END = "|@";
    static String TOKEN_INIT = "@|";

    public static void print(String message, Color color) {
        System.out.println(ansi().render(TOKEN_INIT + color + " " + message + " " + TOKEN_END));
    }

}
