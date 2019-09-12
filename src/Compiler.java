import java.io.FileInputStream;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws IOException {

        SymbolTable.init();

//        String path = args[0];
        String source_path = "test/comments_test.txt";
        FileInputStream source_file = new FileInputStream(source_path);
        StringBuffer sb = new StringBuffer();

        while (source_file.available() != 0)
            sb.append((char) source_file.read());
        source_file.close();

        Lexer al = new Lexer(sb);

        int out = 1;
        while (out != 0) { // 0 es EOF
            out = al.yylex();
        }
    }


}