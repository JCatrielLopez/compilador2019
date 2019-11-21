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
                    return (e.getLex() + " dw "+ (Integer.valueOf(e.getAttr("size"))+1) + " dup (?)");
                else
                    return (e.getLex() + " dd "+ (Integer.valueOf(e.getAttr("size"))+1)+ " dup (?)");
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

            String op1 = t.getOperando1();

            if (op1 != null) {
                Token token = SymbolTable.getLex(op1);
                if (token != null) { // Reescribo los id de las variables.
                    if (token.getAttr("use") != null)
                        if (token.getAttr("use").equals("VARIABLE") || token.getAttr("use").equals("COLECCION")){
                            t.setOperando1("_" + op1);}
                } else {
                    if ((!op1.startsWith("[")) && op1.endsWith("]")){
                        t.setOperando1("_" + op1);}
                }
            }

            String op2 = t.getOperando2();

            if (op2 != null) {
                Token token = SymbolTable.getLex(op2);
                if (token != null) { // Reescribo los id de las variables.
                    if (token.getAttr("use") != null)
                        if (token.getAttr("use").equals("VARIABLE") || token.getAttr("use").equals("COLECCION"))
                            t.setOperando2("_" + op2);
                } else {
                    if (!op2.startsWith("[") && op2.endsWith("]"))
                        t.setOperando2("_" + op2);
                }
            }
        }

        HashMap<String, Token> new_tokens = new HashMap<>();
        for (String lex : SymbolTable.keys()) {
            Token token = SymbolTable.getLex(lex);
            if (!token.getDescription().equals("CADENA"))
                if (token.getAttr("use").equals("VARIABLE") || token.getAttr("use").equals("COLECCION")) {
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
        if (operando.startsWith("[")) {
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
        writer.append("include \\masm32\\include\\masm32rt.inc");
        writer.append("\n");
        writer.append("dll_dllcrt0 PROTO C");
        writer.append("\n");
        writer.append("printf PROTO C :VARARG");
        writer.append("\n\n");

        // Declaracion de variables

        writer.append(".data");
        writer.append("\n\n");
        writer.append(";Datos del programa. \n");
        String declaration = "";
        for (String s : SymbolTable.keys()) {
            declaration = declare(s);
            if (!declaration.equals("")) {
                writer.append(declare(s));
                writer.append("\n");
            }
        }


        //variables auxiliares
        writer.append("\n");
        writer.append(";Variables predefinidas. \n");
        writer.append("@resultado dw ?")
            .append("\n")
            .append("@coleccion dw ?")
            .append("\n")
            .append("@indice dw ?")
            .append("\n")
            .append("@tipo dw ?")
            .append("\n")
            .append("@tempEAX dd ?")
            .append("\n")
            .append("@tempEBX dd ?")
            .append("\n")
            .append("@tempECX dd ?")
            .append("\n")
            .append("@tempEDX dd ?")
            .append("\n");

        writer.append("ConvError db \"Error, perdida de informacion en conversion.\", 0");
        writer.append("\n");
        writer.append("IndiceError db \"Error, indice fuera de los limites de la coleccion.\", 0");
        writer.append("\n\n");

        // Codigo

        writer.append(".code");
        writer.append("\n\n");

        writer.append("error_negativo:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr ConvError, addr ConvError, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append("\n\n");

        writer.append("error_indice:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr IndiceError, addr IndiceError, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append("\n\n");

        //AdminRegistros ar = AdminRegistros.getInstance();//TODO para mi esto genera conflictos xq no sabe q registro esta disponible y le pisa el contenido
        //funciones predefinidas
        writer.append("_length:");
        writer.append("\n");
        writer.append("MOV AX, [@coleccion]");
        writer.append("\n");
        writer.append("MOV @resultado, AX");
        writer.append("\n");
        writer.append("RET");
        writer.append("\n\n");

        writer.append("_first:");
        writer.append("\n");
        writer.append("MOV AX, [@coleccion]");
        writer.append("\n");
        writer.append("MOV @resultado, AX");
        writer.append("\n");
        writer.append("RET");
        writer.append("\n\n");

        writer.append("_last:");
        writer.append("\n");
        writer.append("MOV AX, [@coleccion]");
        writer.append("\n");
        writer.append("MOV @resultado, AX");
        writer.append("\n");
        writer.append("RET");
        writer.append("\n\n");

        writer.append("_elemento:");
        writer.append("\n");
        writer.append("CALL _get_offset");
        writer.append("\n");
        writer.append("MOV AX, @resultado");
        writer.append("\n");
        writer.append("MOV BX, @coleccion");
        writer.append("\n");
        writer.append("MOV @resultado, [BX+AX]");
        writer.append("\n");
        writer.append("RET");
        writer.append("\n\n");

        writer.append("_get_offset:");
        writer.append("\n");
        writer.append("MOV AX, @indice");//pongo indice en AX
        writer.append("\n");
        writer.append("CMP AX, 0");
        writer.append("\n");
        writer.append("JL error_indice"); //indice negativo
        writer.append("\n");
        writer.append("CMP AX, [@coleccion]"); //comparo con tamaño
        writer.append("\n");
        writer.append("JG error_indice"); // indice mayor a tañano de la coleccion
        writer.append("\n");
        writer.append("MUL AX, @tipo"); //calculo el offset
        writer.append("\n");
        writer.append("MOV @resultado, AX"); //retorno el resultado
        writer.append("\n");
        writer.append("RET");
        writer.append("\n\n");

        writer.append("start:");
        writer.append("\n\n");

        //Setear tamaño arreglos
        writer.append(";Seteamos los tamaños de las colecciones. \n");
        for (String lex : SymbolTable.keys()) {
            if(SymbolTable.getLex(lex).getAttr("use")!= null && SymbolTable.getLex(lex).getAttr("use").equals("COLECCION")){
                writer.append("MOV ["+ lex + "], "+SymbolTable.getLex(lex).getAttr("size"));
                writer.append("\n");
            }
        }
        writer.append(";Fin de seteo de tamaños. \n");

        for (Terceto t : AdminTercetos.list()) {
            writer.append(getCode(t));
        }


        writer.append("invoke ExitProcess, 0");
        writer.append("\n\n");
        writer.append("end start");

        writer.close();

        fileOutput.close();

    }

    private static String getCode(Terceto t) {

        StringBuilder instructions = new StringBuilder();
        AdminRegistros ar = AdminRegistros.getInstance();

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
                String nroLabel = t.getOperando1().substring(1, t.getOperando1().length() - 1);
                instructions.append("JMP Label" + nroLabel)
                            .append("\n");
                break;
            case "BF":
                Terceto anterior = AdminTercetos.get(t.getOperando1());
                String target = t.getOperando2().substring(1, t.getOperando2().length() - 1);
                target = "Label" + target;

                switch (anterior.getOperacion()){
                    case ">":
                        instructions.append("JLE " + target);
                        break;
                    case "<":
                        instructions.append("JGE " + target);
                        break;
                    case ">=":
                        instructions.append("JL " + target);
                        break;
                    case "<=":
                        instructions.append("JG " + target);
                        break;
                    case "==":
                        instructions.append("JNE " + target);
                        break;
                    case "<>":
                        instructions.append("JE " + target);
                        break;
                }
                instructions.append("\n");
                break;
            case "Label":
                instructions.append("Label" + t.getOperando1().substring(1, t.getOperando1().length() - 1) + ":")
                            .append("\n");
                break;
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "<>":
            case "==":
                if(tipo_op1.equals("variable") && tipo_op2.equals("variable")){ //si ambos operandos estan en mem traer uno a reg
                    reg_A = ar.getRegBC(size);
                    instructions.append("MOV ")
                            .append(reg_A)
                            .append(", ")
                            .append(getOP(t.getOperando1()))
                            .append("\n");
                    ar.free(reg_A);
                    reg_B = getOP(t.getOperando2());
                } else{ //si uno no esta en mem opero, y libero si corresponde
                    reg_A = getOP(t.getOperando1());
                    reg_B = getOP(t.getOperando2());
                    if(tipo_op1.equals("terceto")){
                        ar.free(reg_A);
                    }
                    if(tipo_op2.equals("terceto")){
                        ar.free(reg_B);
                    }
                }
                instructions.append("CMP " + reg_A + ", " + reg_B)
                            .append("\n");
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

                if (!SymbolTable.getLex(t.getOperando1()).getAttr("use").equals("COLECCION")) {
                    instructions.append("MOV ")
                            .append(reg_A)
                            .append(", ")
                            .append(reg_B)
                            .append("\n");
                } else {
                    reg_A = ar.getRegAD(32);
                    instructions.append("LEA ")
                            .append(reg_A)
                            .append(", ")
                            .append("[" + getOP(t.getOperando1()) + "]")
                            .append("\n")
                            .append("MOV @coleccion, ")
                            .append(reg_A)
                            .append("MOV @tipo, ")
                            .append(size / 8)
                            .append("\n")
                            .append("MOV @indice, 1");

                    instructions.append("CALL _rowing");
                }


                break;
            case "PRINT":
                Token token = SymbolTable.getLex(t.getOperando1());
                if(!token.getDescription().equals("CADENA")){
                    if(!token.getAttr("use").equals("COLECCION")){
                        instructions.append("invoke printf, cfm$(\"%. %d\\n\"), "+ getOP(t.getOperando1()))
                                    .append("\n");
                    }else {//TODO print coleccion

                    }
                }else
                {
                    String operando = getOP(t.getOperando1());

                    instructions.append("INVOKE MessageBox, NULL, addr ")
                            .append(operando)
                            .append(", addr ")
                            .append(operando)

                            .append(", MB_OK")
                            .append("\n");

                }
                break;
            case "_CONV":
                if(tipo_op1.equals("variable")){ // si es variable la traigo a reg
                    reg_A = ar.getRegBC(16); //TODO creo q CWDE solo funciona si el registro es AX
                    instructions.append("MOV ")
                            .append(reg_A)
                            .append(", ")
                            .append(getOP(t.getOperando1()))
                            .append("\n");
                }else{
                    reg_A = getOP(t.getOperando1());
                }
                //Chequeo perida de informacion
                instructions.append("CMP ")
                        .append(reg_A)
                        .append(", ")
                        .append("0")
                        .append("\n");
                instructions.append("JL error_negativo")
                        .append("\n");
                //si no es negativo realizo la conversion
                instructions.append("CWDE ")
                        .append("\n");
                t.setRegister("E"+reg_A);
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
