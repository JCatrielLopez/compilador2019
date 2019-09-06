import java.io.FileInputStream;
import java.io.IOException;

public class Compiler {

    public static void main(String[] args) throws IOException {

        SymbolTable.init();

//        String path = args[0];
        String path = "test/id_test.txt";
        FileInputStream archivo = new FileInputStream(path);
        StringBuffer sb = new StringBuffer();

        while (archivo.available() != 0)
            sb.append((char) archivo.read());
        archivo.close();

        Lexer al = new Lexer(sb);

        al.yylex();

    }


}