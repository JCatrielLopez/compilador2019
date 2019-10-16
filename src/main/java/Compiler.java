import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class Compiler {

  public static void main(String[] args) throws Exception {
    AnsiConsole.systemInstall();
    SymbolTable.init();

    String source_path = "";
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
            if (!tempFile.exists()) throw new IOException("Input file does not exist.");
          } else throw new Exception("Input file not found.");
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
        case "-t":
          ts_verbose = true;
          break;
        case "-h":
          if (!(al_verbose || as_verbose || gc_verbose || ts_verbose)) print_help();
          return;
        default:
          // Ignoramos los argumentos que no son validos.
      }
    }

    FileInputStream source_file = new FileInputStream(source_path);
    StringBuffer sb = new StringBuffer();

    while (source_file.available() != 0) sb.append((char) source_file.read());
    source_file.close();

    Lexer al = new Lexer(sb, al_verbose);

    int out = 1;
      System.out.println(
              String.format(
                      "%5s %s %3s %s %s %-50s %s %s",
                      "Linea ", "|", "Desde", "|", " ", "Info", " ", "|"));
      System.out.println("-----------------------------------------------------------------------");

    Parser parser = new Parser(al, as_verbose);
    parser.run();

    if (ts_verbose) SymbolTable.print();

    AnsiConsole.systemUninstall();
  }

  private static void print_help() {
    System.out.println("Usage: java -jar COMPILADOR [OPTIONS] (-i SOURCE_FILE | -h)\n");
    System.out.println("Compilador para Diseño de Compiladores 2019");
    System.out.println("Velez, Ezequiel\nLopez, Catriel\n");
    System.out.println(String.format("%6s %s %-30s", "Option", " ", "Description"));
    System.out.println(String.format("%6s %s %-30s", "-i", " ", "Input file."));
    System.out.println(
            String.format("%6s %s %-30s", "-l", " ", "Print information from lexical analysis."));
    System.out.println(
            String.format("%6s %s %-30s", "-s", " ", "Print information from syntactic analysis."));
    System.out.println(
            String.format("%6s %s %-30s", "-g", " ", "Print information from assembler generation"));
    System.out.println(String.format("%6s %s %-30s", "-t", " ", "Print Symbol table"));
    System.out.println(
            String.format(
                    "%6s %s %-30s",
                    "-h", " ", "Print help information. Fails if use with more arguments."));
  }
}
