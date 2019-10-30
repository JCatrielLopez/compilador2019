package globals;

import static org.fusesource.jansi.Ansi.ansi;

public class Printer {

    public static void print(String message, Color color) {
        String TOKEN_END = "|@";
        String TOKEN_INIT = "@|";
        System.out.println(ansi().render(TOKEN_INIT + color + " " + message + " " + TOKEN_END));
    }

}
