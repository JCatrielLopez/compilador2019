import assembler.AdminTercetos;
import assembler.AssemblerGen;
import globals.Error;
import globals.SymbolTable;
import lexer.Lexer;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.fusesource.jansi.AnsiConsole;
import parser.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Compiler {

  public static void main(String[] args) throws Exception {
    AnsiConsole.systemInstall();
    SymbolTable.init();

    Options options = new Options();
    options.addOption("i", true, "Direccion del archivo a compilar.");
    options.addOption("al", false, "Imprime informacion del analisis lexico.");
    options.addOption("as", false, "Imprime informacion del analisis sintactico.");
    options.addOption("ts", false, "Imprime informacion de la tabla de simbolos.");
    options.addOption("t", false, "Imprime informacion de la generacion de tercetos.");
    options.addOption("c", false, "Genera codigo assembly y traduce a codigo maquina.");
    options.addOption("h", false, "Imprime informacion de ayuda.");

    CommandLineParser cmdparser = new DefaultParser();
    CommandLine cmd = cmdparser.parse(options, args);

    if (cmd.hasOption("h")) {
      new HelpFormatter().printHelp("compiler", options);
      return;
    }

    FileInputStream source_file;
    String source_path;
    if (cmd.hasOption("i")) {
      source_path = cmd.getOptionValue("i");
      File tempFile = new File(source_path);
      if (!tempFile.exists())
        throw new IOException("No existe el archivo " + source_path);
      else
        source_file = new FileInputStream(source_path);
    } else
      throw new ParseException("Falta el argumento -i.");

    StringBuilder sb = new StringBuilder();

    while (source_file.available() != 0) sb.append((char) source_file.read());
    source_file.close();

    if (cmd.hasOption("al") || cmd.hasOption("as")) {
      System.out.println(
              String.format(
                      "%5s %s %3s %s %s %-50s %s %s", "Linea ", "|", "Desde", "|", " ", "Info", " ", "|"));
      System.out.println("-----------------------------------------------------------------------");
    }

    Lexer al = new Lexer(sb, cmd.hasOption("al"));

    Parser parser = new Parser(al, cmd.hasOption("as"));
    parser.run();

    if (!Error.isEmpty()) {
      System.out.println("-----------------------------------------------------------------------");
      System.out.println("-------------------------ERRORS AND WARNINGS---------------------------");
      System.out.println("-----------------------------------------------------------------------");
      System.out.println(
              String.format(
                      "%5s %s %3s %s %s %-50s %s %s", "Linea ", "|", "Desde", "|", " ", "Info", " ", "|"));
      System.out.println("-----------------------------------------------------------------------");
      Error.print();
    } else {
      if (cmd.hasOption("t")) {
        System.out.println(
                "-----------------------------------------------------------------------");
        System.out.println(
                "-------------------------------TERCETOS--------------------------------");
        System.out.println(
                "-----------------------------------------------------------------------");
        System.out.println(AdminTercetos.print());
      }

      if (cmd.hasOption("c")) {
        String source_name = FilenameUtils.removeExtension(source_path);
        String tercetos_path = source_name + ".t";
        String assembler_path = source_name + ".asm";
        String obj_path = source_name + ".obj";

        // Guardo los tercetos en archivo.t
        FileOutputStream tercetos_file = new FileOutputStream(tercetos_path);

        tercetos_file.write(AdminTercetos.print().getBytes());

        // Genero las instrucciones assembly
        AssemblerGen.translate(assembler_path);

        // Genero el archivo exe
        Process p;

        p = Runtime.getRuntime().exec("\\masm32\\bin\\ml /c /Zd /coff " + assembler_path);
        p = Runtime.getRuntime().exec("\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE" + obj_path);
      }
    }

    if (cmd.hasOption("ts")) SymbolTable.print();

    AnsiConsole.systemUninstall();
  }

  private static void print_help() {
    System.out.println("Usage: java -jar COMPILADOR [OPTIONS] (-i SOURCE_FILE | -h)\n");
    System.out.println("Compilador para Diseño de Compiladores 2019");
    System.out.println("Velez, Ezequiel\nLopez, Catriel\n");
    System.out.println(String.format("%6s %s %-30s", "Option", " ", "Description"));
    System.out.println(String.format("%6s %s %-30s", "-i", " ", "Input file."));
    System.out.println(
            String.format("%6s %s %-30s", "-al", " ", "Imprime informacion del analisis lexico."));
    System.out.println(
            String.format("%6s %s %-30s", "-as", " ", "Imprime informacion del analisis sintactico."));
    System.out.println(
            String.format(
                    "%6s %s %-30s", "-g", " ", "Imprime informacion de la generacion de assembler."));
    System.out.println(String.format("%6s %s %-30s", "-ts", " ", "Imprime la tabla de simbolos."));
    System.out.println(String.format("%6s %s %-30s", "-h", " ", "Imprime informacion de ayuda."));
  }
}
