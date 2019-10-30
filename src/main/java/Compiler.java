import globals.SymbolTable;
import lexer.Lexer;
import org.apache.commons.cli.*;
import org.fusesource.jansi.AnsiConsole;
import parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class Compiler {
  //TODO Crear una opcion para mandar los resultados  a un archivo.
  public static void main(String[] args) throws Exception {
    AnsiConsole.systemInstall();
    SymbolTable.init();

    String source_path = "";
    boolean al_verbose = false;
    boolean as_verbose = false;
    boolean ts_verbose = false;

    Options options = new Options();
    options.addOption("i", true, "Direccion del archivo a compilar.");
    options.addOption("al", false, "Imprime informacion del analisis lexico.");
    options.addOption("as", false, "Imprime informacion del analisis sintactico.");
    options.addOption("ts", false, "Imprime informacion de la tabla de simbolos.");
    options.addOption("h", false, "Imprime informacion de ayuda.");

    CommandLineParser cmdparser = new DefaultParser();
    CommandLine cmd = cmdparser.parse(options, args);

    if (cmd.hasOption("h")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("compiler", options);
      return;
    }

    if (cmd.hasOption("i")) {
      source_path = cmd.getOptionValue("i");
      File tempFile = new File(source_path);
      if (!tempFile.exists()) throw new IOException("No existe el .");
    } else
      return;

    if (cmd.hasOption("al"))
      al_verbose = true;
    if (cmd.hasOption("as"))
      as_verbose = true;
    if (cmd.hasOption("ts"))
      ts_verbose = true;

    FileInputStream source_file = new FileInputStream(source_path);

    StringBuilder sb = new StringBuilder();

    //TODO  Leer en el lexico?
    while (source_file.available() != 0) sb.append((char) source_file.read());
    source_file.close();

    Lexer al = new Lexer(sb, al_verbose);

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
}
