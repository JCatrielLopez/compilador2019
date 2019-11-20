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

        if (e.getID() == SymbolTable.getID("id")) {
            if(e.getAttr("use").equals("VARIABLE")){
                if (e.getAttr("type").equals("INT"))
                    return (e.getLex() + " dw ?");
                else
                    return (e.getLex() + " dd ?");
            } else if (e.getAttr("use").equals("COLECCION")) {
                if (e.getAttr("type").equals("INT"))
                    return (e.getLex() + " dw "+ e.getAttr("size") + " dup ?");
                else
                    return (e.getLex() + " dd "+ e.getAttr("size")+ " dup ?");
            }

        }

        if (e.getID() == SymbolTable.getID("cte")) {
            String nombreCte = "cte" + ctes.size();
            ctes.put(e.getLex(), nombreCte);
            if (e.getAttr("type").equals("INT"))
                return (nombreCte + " dw " + e.getLex());
            else
                return (nombreCte + " dd " + e.getLex());
        }

        if (e.getID() == SymbolTable.getID("cadena")) {
            String nombreCad = "cadena" + cadenas.size();
            cadenas.put(e.getLex(), nombreCad);
            return (nombreCad + " db " +'"'+ e.getLex() +'"'+ ", 0");
        }



        return "";
    }

    public static void redefineVariables() {

        for (Terceto t : AdminTercetos.list()) {
            if (!t.getOperacion().equals("label")) {
                //TODO si el operando empieza con _ (calls) se rompe
                if (!t.getOperando1().startsWith("[")) {
                    if (!SymbolTable.getLex(t.getOperando1()).getDescription().equals("CADENA"))
                        if (SymbolTable.getLex(t.getOperando1()).getAttr("use").equals("VARIABLE"))
                            t.setOperando1("_" + t.getOperando1());
                }
                if (t.getOperando2() != null)
                    if (!t.getOperando2().startsWith("[")) {
                        if (!SymbolTable.getLex(t.getOperando2()).getDescription().equals("CADENA"))
                            if (SymbolTable.getLex(t.getOperando2()).getAttr("use").equals("VARIABLE"))
                                t.setOperando2("_" + t.getOperando2());
                    }
            }
        }

        HashMap<String, Token> new_tokens = new HashMap<>();
        for (String lex : SymbolTable.keys()) {
            Token token = SymbolTable.getLex(lex);
            if (!token.getDescription().equals("CADENA"))
                if (token.getAttr("use").equals("VARIABLE")) {
                    token.setLex("_" + lex);
                    new_tokens.put("_" + lex, token);
                } else
                    new_tokens.put(lex, token);
            else
                new_tokens.put(lex, token);
        }
        SymbolTable.setSymbols(new_tokens.clone());
    }

    // Devuelve el operando de la instruccion en assembler, sea el registro en el que esta almacenado el
    // resultado del terceto, el nombre de la variable o la constante.
    public static String getOP(String operando) {
        if (operando.charAt(0) == '[') {
            return AdminTercetos.get(operando)
                    .getRegister();
        }

        Token token = SymbolTable.getLex(operando);
        if (token != null) {
            String uso = token.getAttr("use");
            if (uso != null)
                switch (uso) {
                    case "VARIABLE":
                        return operando;
                    case "CTE NEG":
                    case "CTE POS":
                        return ctes.get(operando);
                }
            else // Es una cadena!
                return cadenas.get(operando);
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

        //TODO aca van las funciones predefinidas first, last, leght, (acceder a un elemento coleccion?)
        //TODO Generar assembler para las funciones FIRST, LENGTH y LAST.
        writer.append("start:");
        writer.append("\n");


        for (Terceto t : AdminTercetos.list()) {
            writer.append(getCode(t));
        }


        writer.append("invoke ExitProcess, 0");
        writer.append("\n");
        writer.append("end start");

        writer.close();

        fileOutput.close();

    }

    private static String getCode(Terceto t) {

        StringBuilder instructions = new StringBuilder();
        AdminRegistros ar = AdminRegistros.getInstance();
        //ar.imp();
        //System.out.println("-----------------------------");

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
                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro... opero sobre el segundo operando
                        reg_A = getOP(t.getOperando2());
                        reg_B = getOP(t.getOperando1());
                    }
                } else { //primer operando registro no importa lo q sea el segundo.. opero sobre el primero
                    reg_A = getOP(t.getOperando1());
                    reg_B = getOP(t.getOperando2());
                    if(tipo_op2.equals("terceto")){//si ambos son terceto libero al segundo
                        ar.free(reg_B);
                    }
                }
                instructions.append("ADD ")
                        .append(reg_A)
                        .append(", ")
                        .append(reg_B)
                        .append("\n");

                t.setRegister(reg_A);

                break;
            case "-":
                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro...  ocupo nuevo reg, genero codigo sobre ese reg y libero el otro
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                        ar.free(reg_B);
                    }
                } else { //primer operando registro no importa lo q sea el segundo.. opero sobre el primero
                    reg_A = getOP(t.getOperando1());
                    reg_B = getOP(t.getOperando2());
                    if(tipo_op2.equals("terceto")){//si ambos son terceto libero al segundo
                        ar.free(reg_B);
                    }
                }
                instructions.append("SUB ")
                        .append(reg_A)
                        .append(", ")
                        .append(reg_B)
                        .append("\n");

                t.setRegister(reg_A);

                break;
            case "/":
                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro...  ocupo nuevo reg, genero codigo sobre ese reg y libero el otro
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                        ar.free(reg_B);
                    }
                } else { //primer operando registro no importa lo q sea el segundo.. opero sobre el primero
                    reg_A = getOP(t.getOperando1());
                    reg_B = getOP(t.getOperando2());
                    if(tipo_op2.equals("terceto")){//si ambos son terceto libero al segundo
                        ar.free(reg_B);
                    }
                }
                instructions.append("IDIV ")
                        .append(reg_A)
                        .append(", ")
                        .append(reg_B)
                        .append("\n");

                t.setRegister(reg_A);

                break;
            case "*":
                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegAD(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro... opero sobre el segundo operando
                        reg_A = getOP(t.getOperando2());
                        reg_B = getOP(t.getOperando1());
                    }
                } else { //primer operando registro no importa lo q sea el segundo.. opero sobre el primero
                    reg_A = getOP(t.getOperando1());
                    reg_B = getOP(t.getOperando2());
                    if(tipo_op2.equals("terceto")){//si ambos son terceto libero al segundo
                        ar.free(reg_B);
                    }
                }
                instructions.append("IMUL ")
                    .append(reg_A)
                    .append(", ")
                    .append(reg_B)
                    .append("\n");

                t.setRegister(reg_A);

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
                if(tipo_op2.equals("variable")) { //valor a asignar en variable
                    reg_B = ar.getRegBC(size);
                    instructions.append("MOV ")
                            .append(reg_B)
                            .append(", ")
                            .append(getOP(t.getOperando2()))
                            .append("\n");
                    reg_A = getOP(t.getOperando1());
                    ar.free(reg_B);
                }else{//valor a asignar en registro
                    reg_A =  getOP(t.getOperando1());
                    reg_B =  getOP(t.getOperando2());
                    ar.free(reg_B);
                }
                instructions.append("MOV ")
                        .append(reg_A)
                        .append(", ")
                        .append(reg_B)
                        .append("\n");

                break;
            case "PRINT":
                String operando = getOP(t.getOperando1());
                instructions.append("INVOKE MessageBox, NULL, addr ")
                        .append(operando)
                        .append(", addr ")
                        .append(operando)
                        .append(", MB_OK")
                        .append("\n");
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
