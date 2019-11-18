package assembler;

import globals.SymbolTable;
import lexer.Token;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public final class AssemblerGen {

    private static HashMap<String, String> ctes = new HashMap<String, String>();
    private static HashMap<String, String> cadenas = new HashMap<String, String>();


    public static String declare(String lex) {
        Token e = SymbolTable.getLex(lex);

        if (e.getID() == SymbolTable.getID("id") && e.getAttr("use").equals("variable")) {
            if (e.getAttr("type").equals("INT"))
                return (e.getLex() + " dw ?");
            else
                return (e.getLex() + " dd ?");
        }

        if (e.getID() == SymbolTable.getID("cadena")) {
            String cadena = "cadena" + cadenas.size();
            cadenas.put(e.getLex(), cadena);
            return (cadena + " db " + lex + ", 0");
        }


        return "";
    }

    public static void redefineVariables() {

        // TODO Revisar: Creo que es innecesario renombrar los tercetos
//        for (Terceto t : AdminTercetos.tercetos) {
//            if (t.getOperacion() != "label") {
//                if (!t.getOperando1().startsWith("[")) {
//                    t.setOperando1("_" + t.getOperando1());
//                }
//                if (!t.getOperando2().startsWith("[")) {
//                    t.setOperando2("_" + t.getOperando2());
//                }
//            }
//        }

        for (String lex : SymbolTable.keys()) {
            Token token = SymbolTable.getLex(lex);
            if (token.getID() == (SymbolTable.getID("id"))) {
                SymbolTable.remove(lex);
                token.setLex("_" + lex);
                SymbolTable.add(token);
            }
        }

    }

    public static void translate(String path) throws Exception {

        redefineVariables();
// TODO        AdminTercetos.agregarEtiquetas();

        FileOutputStream fileOutput = new FileOutputStream(path);
        OutputStreamWriter writer = new OutputStreamWriter(fileOutput);

        writer.append(".386");
        writer.append("\n");
        writer.append(".model flat, stdcall");
        writer.append("\n");
        writer.append("option casemap :none");
        writer.append("\n");
        writer.append("include \\masm32\\include\\windows.inc");
        writer.append("\n");
        writer.append("include \\masm32\\include\\kernel32.inc");
        writer.append("\n");
        writer.append("include \\masm32\\include\\user32.inc");
        writer.append("\n");
        writer.append("includelib \\masm32\\lib\\kernel32.lib");
        writer.append("\n");
        writer.append("includelib \\masm32\\lib\\user32.lib");
        writer.append("\n\n");

        // Declaracion de variables

        writer.append(".data");
        writer.append("\n");
        for (String s : SymbolTable.keys()) {
            writer.append(declare(s));
            writer.append("\n");
        }

        writer.append("OverFlow db \"OverFlow en operacion aritmetica\", 0");
        writer.append("\n");
        writer.append("DivPorCero db \"No se permite dividir por cero\", 0");
        writer.append("\n\n");


        // Codigo

        writer.append(".code");
        writer.append("\n");


        writer.append("overflow:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr OverFlow, addr OverFlow, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append("\n");

        writer.append("divcero:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr DivPorCero, addr DivPorCero, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append("\n\n");

        writer.append("start:");
        writer.append("\n");


        for (Terceto t : AdminTercetos.list()) {
            writer.append(getCode(t));
        }

        //TODO Generar assembler para las funciones FIRST, LENGTH y LAST.

        writer.append("invoke ExitProcess, 0");
        writer.append("\n");
        writer.append("end start");

        writer.close();

        fileOutput.close();

    }

    private static String getCode(Terceto t) {

        StringBuilder instructions = new StringBuilder();

        String reg_A = "";
        String reg_B = "";

        String tipo_op1 = use(t.getOperando1());
        String tipo_op2 = use(t.getOperando2());

        int size;

        if (t.type().equals("ULONG"))
            size = 32;
        else
            size = 16;

        switch (t.getOperacion()) {
            case "+":
                //TODO Generar assembler para la operacion +
                break;
            case "-":
                //TODO Generar assembler para la operacion -
                break;
            case "/":
                //TODO Generar assembler para la operacion /
                break;
            case "*":
                //TODO Generar assembler para la operacion *
                break;
            case "BI":
                //TODO Generar assembler para la operacion BI
                break;
            case "BF":
                //TODO Generar assembler para la operacion BF
                break;
            case "label":
                //TODO Generar assembler para las labels
                break;
            case ":=":
                //TODO Generar assembler para la operacion :=
                break;
            case "PRINT":
                //TODO Generar assembler para la operacion PRINT
                break;
            case "_CONV":
                //TODO Generar assembler para la operacion _CONV
                break;
        }

        return instructions.toString();
    }

    private static String use(String op) {
        if (op != null) {
            if (op.startsWith("["))
                return "terceto";
            return "variable"; // return SymbolTable.getLex(op).getAttr("use");
        }
        return "";

    }
}
