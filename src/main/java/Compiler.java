import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class Compiler {


    public static void main(String[] args) throws Exception {
        AnsiConsole.systemInstall();
        SymbolTable.init();

        String source_path = "";
        String output_path = "";
        boolean al_verbose = false;
        boolean as_verbose = false;
        boolean gc_verbose = false;
        boolean ts_verbose = false;


        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                    if (i + 1 < args.length) {
                        source_path = args[i + 1];
                        File tempFile = new File(source_path);
                        if (!tempFile.exists())
                            throw new IOException("File does not exist.");
                    } else
                        throw new Exception("Input file not declared.");
                    break;
                case "-l":
                    al_verbose = true;
                    break;
                case "-s":
                    as_verbose = true;
                    break;
                case "-g":
                    gc_verbose = true;
                    break;
                case "-o":
                    if (i + 1 < args.length)
                        output_path = args[i + 1];
                    break;
                case "-t":
                    ts_verbose = true;
                    break;
                default:
                    // Ignoramos los argumentos que no son validos.
                    System.out.println("Invalid argument.");
            }
        }

//        source_path = "test/id_test.txt";
        FileInputStream source_file = new FileInputStream(source_path);
        StringBuffer sb = new StringBuffer();

        while (source_file.available() != 0)
            sb.append((char) source_file.read());
        source_file.close();

        Lexer al = new Lexer(sb, al_verbose);

        int out = 1;
        if (al_verbose) {
            System.out.println(String.format("%5s %s %5s %s %8s %10s %10s %s %10s", "Line ", "|", "Token",
                    "|", " ", "Lexeme", " ", "|", "Description"));
            System.out.println("------------------------------------------------------------");
        }

        //TODO Hay que arreglar el camino del '='. No es un literal, es un caracter invalido!!!
        while (out != 0) { // 0 es EOF
            out = al.yylex();
        }

        if (ts_verbose)
            SymbolTable.print();
        AnsiConsole.systemUninstall();
    }


}