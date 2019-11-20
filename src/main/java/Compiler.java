import assembler.AdminTercetos;
import assembler.AssemblerGen;
import globals.Error;
import globals.Printer;
import globals.SymbolTable;
import lexer.Lexer;
import org.apache.commons.io.FilenameUtils;
import org.fusesource.jansi.AnsiConsole;
import parser.Parser;

import java.io.*;

class Compiler {

  public static void main(String[] args) throws Exception {
    AnsiConsole.systemInstall();
    SymbolTable.init();

    String source_path = "";
    boolean al_verbose = false;
    boolean as_verbose = false;
    boolean gc_verbose = false;
    boolean ts_verbose = false;
    boolean tercetos_verbose = false;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-i":
          if (i + 1 < args.length) {
            source_path = args[i + 1];
            File tempFile = new File(source_path);
            if (!tempFile.exists()) {
              Printer.print("El archivo ingresado no existe.");
              return;
            }
          } else {
            Printer.print("No se ingreso ningun archivo.");
            return;
          }
          break;
        case "-al":
          al_verbose = true;
          break;
        case "-as":
          as_verbose = true;
          break;
        case "-g":
          gc_verbose = true;
          break;
        case "-ts":
          ts_verbose = true;
          break;
        case "-t":
          tercetos_verbose = true;
          break;
        case "-h":
          if (!(al_verbose || as_verbose || gc_verbose || ts_verbose || tercetos_verbose))
            print_help();
          return;
        default:
          // Ignoramos los argumentos que no son validos.
      }
    }

    FileInputStream source_file = new FileInputStream(source_path);

    StringBuilder sb = new StringBuilder();

    while (source_file.available() != 0) sb.append((char) source_file.read());
    source_file.close();

    if (al_verbose || as_verbose) {
      System.out.println(
              String.format(
                      "%5s %s %3s %s %s %-50s %s %s", "Linea ", "|", "Desde", "|", " ", "Info", " ", "|"));
      System.out.println("-----------------------------------------------------------------------");
    }

    Lexer al = new Lexer(sb, al_verbose);

    Parser parser = new Parser(al, as_verbose);
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
      if (tercetos_verbose) {
        System.out.println(
                "-----------------------------------------------------------------------");
        System.out.println(
                "-------------------------------TERCETOS--------------------------------");
        System.out.println(
                "-----------------------------------------------------------------------");
        System.out.println(AdminTercetos.print());
      }

      String source_name = "tests/asd";
      String tercetos_path = source_name + ".t";
      String assembler_path = source_name + ".asm";
      String obj_path = source_name + ".obj";

      // Guardo los tercetos en archivo.t
      FileOutputStream tercetos_file = new FileOutputStream(tercetos_path);

      tercetos_file.write(AdminTercetos.print().getBytes());

      // Genero las instrucciones assembly
      AssemblerGen.translate(assembler_path);

      // Genero el archivo.exe
      Process p = Runtime.getRuntime().exec("\\masm32\\bin\\ml /c /Zd /coff " + assembler_path);
      BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      System.out.println(stdInput.toString());
      System.out.println(stdError.toString());

      p = Runtime.getRuntime().exec("\\masm32\\bin\\Link SUBSYSTEM:CONSOLE" + obj_path);
      stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
      stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

      System.out.println(stdInput.toString());
      System.out.println(stdError.toString());
    }

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
