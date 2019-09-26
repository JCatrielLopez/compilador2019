import static org.fusesource.jansi.Ansi.ansi;

public class Printer {

    private static String TOKEN_INIT = "@|";
    private static String TOKEN_END = "|@";

    public static void print(String message, Color color) {
        System.out.println(ansi().render(TOKEN_INIT + color + " " + message + " " + TOKEN_END));
    }

}
