package globals;

import static org.fusesource.jansi.Ansi.ansi;

public class Printer {
    static String TOKEN_END = "|@";
    static String TOKEN_INIT = "@|";

    public static void print(String message) {
        if (message.contains("ERROR"))
            System.out.println(ansi().render(TOKEN_INIT + Color.RED + " " + message + " " + TOKEN_END));
        else if (message.contains("WARNING"))
            System.out.println(ansi().render(TOKEN_INIT + Color.YELLOW + " " + message + " " + TOKEN_END));
        else
            System.out.println(ansi().render(TOKEN_INIT + Color.RESET + " " + message + " " + TOKEN_END));
    }

}
