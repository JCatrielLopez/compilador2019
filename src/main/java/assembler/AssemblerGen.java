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
        if (e.getID() == SymbolTable.getID("id") && e.getAttr("use").equals("VARIABLE")) {
            if (e.getAttr("type").equals("INT"))
                return (e.getLex() + " dw ?");
            else
                return (e.getLex() + " dd ?");
        }

        if (e.getID() == SymbolTable.getID("cadena")) {
            String cadena = "cte" + cadenas.size();
            cadenas.put(e.getLex(), cadena);
            return (cadena + " db " + lex + ", 0");
        }


        return "";
    }

    public static void redefineVariables() {

        for (Terceto t : AdminTercetos.list()) {
            if (!t.getOperacion().equals("label")) {

                if (!t.getOperando1().startsWith("[")) {
                    t.setOperando1("_" + t.getOperando1());
                }
                if (!t.getOperando2().startsWith("[")) {
                    t.setOperando2("_" + t.getOperando2());
                }
            }
        }

        HashMap<String, Token> new_tokens = new HashMap<>();
        for (String lex : SymbolTable.keys()) {
            Token token = SymbolTable.getLex(lex);
            if (token.getAttr("use").equals("VARIABLE")) {
                token.setLex("_" + lex);
                new_tokens.put("_" + lex, token);
            } else
                new_tokens.put(lex, token);
        }
        SymbolTable.setSymbols(new_tokens.clone());
    }

    // Devuelve el operando de la instruccion en assembler, sea el registro en el que esta almacenado el
    // resultado del terceto, el nombre de la variable o la constante.
    public static String getOP(String operando) {
        if (operando.charAt(0) == '[') {
            return AdminTercetos.get(operando.substring(1, operando.length() - 1))
                    .getRegister();
        }
//        System.out.println(operando + " -> " + SymbolTable.contains(operando));
        Token token = SymbolTable.getLex(operando);
        if (token != null) {
            String uso = token.getAttr("use");
            switch (uso) {
                case "VARIABLE":
                    return operando;
                case "CTE NEG":
                case "CTE POS":
                    return cadenas.get(operando);
            }
        }
        return "";
    }

    public static void translate(String path) throws Exception {

        redefineVariables();

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
        String declaration = "";
        for (String s : SymbolTable.keys()) {
            declaration = declare(s);
            if (!declaration.equals("")) {
                writer.append(declare(s));
                writer.append("\n");
            }
        }


        // Codigo

        writer.append(".code");
        writer.append("\n");

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
        AdminRegistros ar = new AdminRegistros();

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
            //TODO: Verificar que la suma este bien.
            case "+":
                //                ADD dest,src
                //                Operación: dest <- dest + src.
                //                donde dest = {reg|mem} y src = {reg|mem|inmed} no pudiendo ambos operandos
                //                estar en memoria.
                System.out.println("Antes del switch: " + reg_B);
                switch (tipo_op1) {
                    case "terceto":
                        reg_A = getOP(t.getOperando1());
                        reg_B = getOP(t.getOperando2());

                        if (tipo_op2.equals("terceto")) // Libero el registro del resultado del terceto.
                            ar.free(reg_B);

                        break;
                    case "variable":

                        reg_B = getOP(t.getOperando1());
                        System.out.println("Case (Variable): " + reg_B);
                        switch (tipo_op2) {
                            case "terceto":
                                reg_A = getOP(t.getOperando2());
                                ar.free(reg_A);
                                break;
                            case "variable":
                                System.out.println("Case2 (Variable): " + reg_B);
                                reg_A = ar.available(size);
                                instructions.append("MOV ")
                                        .append(reg_A)
                                        .append(", ")
                                        .append(reg_B)
                                        .append("\n");
                                reg_B = getOP(t.getOperando2());
                                break;
                        }
                        break;
                }

                instructions.append("ADD ")
                        .append(reg_A)
                        .append(", ")
                        .append(reg_B)
                        .append("\n")
                        .append("JO overflow")
                        .append("\n");

                t.setRegister(reg_A);

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
