package assembler;

import globals.SymbolTable;
import lexer.Token;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public final class AssemblerGen {

    private static HashMap<String, String> ctes = new HashMap<String, String>();
    private static HashMap<String, String> cadenas = new HashMap<String, String>();

    private static final String RESULTADO_16 = "@resultado_16";
    private static final String RESULTADO_32 = "@resultado_32";
    private static final String COLECCION = "@coleccion";
    private static final String TIPO = "@tipo";
    private static final String OFFSET = "@offset";
    private static final String INDICE = "@indice";
    private static final String VALOR_ASIGNACION_16 = "@valor_asignacion_16";
    private static final String VALOR_ASIGNACION_32 = "@valor_asignacion_32";
    private static final String TEMP_EAX = "@tempEAX";
    private static final String TEMP_EBX = "@tempEBX";
    private static final String TEMP_ECX = "@tempECX";
    private static final String TEMP_EDX = "@tempEDX";


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
                    return (e.getLex() + " dw "+ (Integer.parseInt(e.getAttr("size"))+1) + " dup (?)");
                else
                    return (e.getLex() + " dd "+ (Integer.parseInt(e.getAttr("size"))+1)+ " dup (?)");
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
                    if (token.getAttr("use") != null) {
                        if (token.getAttr("use").equals("VARIABLE") || token.getAttr("use").equals("COLECCION")) {
                            t.setOperando1("_" + op1);
                        }
                    }
                } else {
                    if (!op1.startsWith("[") && !op1.startsWith("_"))
                        t.setOperando1("_" + op1);
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
                    if (!op2.startsWith("["))
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

        if (operando.endsWith("]")){
            return operando.substring(0, operando.lastIndexOf("["));
        }

        Token token = SymbolTable.getLex(operando);
        if (token != null) {
            String uso = token.getAttr("use");
            if (uso != null)
                switch (uso) {
                    case "VARIABLE":
                    case "COLECCION":
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
        writer.append(System.lineSeparator()).append(System.lineSeparator());

        // Declaracion de variables

        writer.append(".data");
        writer.append(System.lineSeparator()).append(System.lineSeparator());
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
        writer.append(RESULTADO_16).append(" dw ?")
            .append(System.lineSeparator())
            .append(RESULTADO_32).append(" dd ?")
            .append(System.lineSeparator())
            .append(COLECCION).append(" dd ?")
            .append(System.lineSeparator())
            .append(INDICE).append(" dw ?")
            .append(System.lineSeparator())
            .append(TIPO).append(" dd ?")
            .append(System.lineSeparator())
            .append(OFFSET).append(" dd ?")
            .append(System.lineSeparator())
            .append(VALOR_ASIGNACION_16).append(" dw ?")
            .append(System.lineSeparator())
            .append(VALOR_ASIGNACION_32).append(" dd ?")
            .append(System.lineSeparator())
            .append(TEMP_EAX).append(" dd ?")
            .append(System.lineSeparator())
            .append(TEMP_EBX).append(" dd ?")
            .append(System.lineSeparator())
            .append(TEMP_ECX).append(" dd ?")
            .append(System.lineSeparator())
            .append(TEMP_EDX).append(" dd ?")
            .append(System.lineSeparator());

        writer.append("ConvError db \"Error, perdida de informacion en conversion.\", 0");
        writer.append("\n");
        writer.append("IndiceError db \"Error, indice fuera de los limites de la coleccion.\", 0");
        writer.append(System.lineSeparator()).append(System.lineSeparator());

        // Codigo

        writer.append(".code");
        writer.append(System.lineSeparator()).append(System.lineSeparator());

        writer.append("error_negativo:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr ConvError, addr ConvError, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append(System.lineSeparator()).append(System.lineSeparator());

        writer.append("error_indice:");
        writer.append("\n");
        writer.append("invoke MessageBox, NULL, addr IndiceError, addr IndiceError, MB_OK");
        writer.append("\n");
        writer.append("invoke ExitProcess, 0");
        writer.append(System.lineSeparator())
          .append(System.lineSeparator());

        writer.append("_length:")
            .append(System.lineSeparator())
            .append("MOV ").append(TEMP_EAX).append(", EAX")
            .append(System.lineSeparator())
            .append("MOV ").append(TEMP_EBX).append(", EBX")
            .append(System.lineSeparator())
            .append("MOV EAX, ").append(COLECCION)
            .append(System.lineSeparator())
            .append("MOV BX, [EAX]")
            .append(System.lineSeparator())
            .append("MOV ").append(RESULTADO_16).append(", BX")
            .append(System.lineSeparator())
            .append("MOV EAX, ").append(TEMP_EAX)
            .append(System.lineSeparator())
            .append("MOV EBX, ").append(TEMP_EBX)
            .append(System.lineSeparator())
            .append("RET")
            .append(System.lineSeparator())
          .append(System.lineSeparator());

        writer.append("_first:")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EAX).append(", EAX")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EBX).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(COLECCION)
                .append(System.lineSeparator())
                .append("ADD EAX, ").append(TIPO)
                .append(System.lineSeparator())
                .append("MOV EBX, [EAX]")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_16).append(", BX")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_32).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(TEMP_EAX)
                .append(System.lineSeparator())
                .append("MOV EBX, ").append(TEMP_EBX)
                .append(System.lineSeparator())
                .append("RET")
                .append(System.lineSeparator()).append(System.lineSeparator());

        writer.append("_last:")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EAX).append(", EAX")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EBX).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(COLECCION)
                .append(System.lineSeparator())
                .append("MOV EBX, 0")
                .append("\n")
                .append("MOV BX, [EAX]")
                .append(System.lineSeparator())
                .append("IMUL EBX, ").append(TIPO)
                .append(System.lineSeparator())
                .append("ADD EAX, EBX")
                .append("\n")
                .append("MOV EBX, [EAX]")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_16).append(", BX")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_32).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(TEMP_EAX)
                .append(System.lineSeparator())
                .append("MOV EBX, ").append(TEMP_EBX)
                .append(System.lineSeparator())
                .append("RET")
                .append(System.lineSeparator()).append(System.lineSeparator());

        writer.append("_element:")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EAX).append(", EAX")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EBX).append(", EBX")
                .append(System.lineSeparator())
                .append("CALL _offset")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(OFFSET)
                .append(System.lineSeparator())
                .append("MOV EBX, [EAX]")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_16).append(", BX")
                .append(System.lineSeparator())
                .append("MOV ").append(RESULTADO_32).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(TEMP_EAX)
                .append(System.lineSeparator())
                .append("MOV EBX, ").append(TEMP_EBX)
                .append(System.lineSeparator())
                .append("RET")
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        writer.append("_offset:")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EAX).append(", EAX")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EBX).append(", EBX")
                .append(System.lineSeparator())
                .append("MOV ").append(TEMP_EDX).append(", EDX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(COLECCION)
                .append(System.lineSeparator())
                .append("MOV EDX, 0")
                .append(System.lineSeparator())
                .append("MOV DX, ").append(INDICE)
                .append(System.lineSeparator())
                .append("CMP DX, 0  ")
                .append(System.lineSeparator())
                .append("JL error_indice")
                .append(System.lineSeparator())
                .append("ADD DX, 1")
                .append(System.lineSeparator())
                .append("MOV EBX, 0")
                .append(System.lineSeparator())
                .append("MOV BX, [EAX]")
                .append(System.lineSeparator())
                .append("CMP DX, BX")
                .append(System.lineSeparator())
                .append("JG error_indice")
                .append(System.lineSeparator())
                .append("IMUL EDX, ").append(TIPO)
                .append(System.lineSeparator())
                .append("ADD EAX, EDX")
                .append(System.lineSeparator())
                .append("MOV ").append(OFFSET).append(", EAX")
                .append(System.lineSeparator())
                .append("MOV EAX, ").append(TEMP_EAX)
                .append(System.lineSeparator())
                .append("MOV EBX, ").append(TEMP_EBX)
                .append(System.lineSeparator())
                .append("MOV EDX, ").append(TEMP_EDX)
                .append(System.lineSeparator())
                .append("RET")
                .append(System.lineSeparator())
          .append(System.lineSeparator());


        writer.append("_rowing:\n")
                .append("MOV ").append(TEMP_EAX).append(", EAX\n")
                .append("MOV ").append(TEMP_EBX).append(", EBX\n")
                .append("MOV ").append(TEMP_ECX).append(", ECX\n")
                .append("CALL _length\n")
                .append("MOV AX, ").append(RESULTADO_16).append(System.lineSeparator())
                .append("SUB AX, 1\n")
                .append("MOV EBX, ").append(COLECCION).append(System.lineSeparator())
                .append("MOV ").append(INDICE).append(", AX\n")
                .append("r_while:\n")
                .append("CMP ").append(INDICE).append(", 0\n")
                .append("JL r_end\n")
                .append("CALL _offset\n")
                .append("MOV ECX, ").append(OFFSET).append(System.lineSeparator())
                .append("CMP ").append(TIPO).append(", 2\n")
                .append("JNE R_asignacion32\n")
                .append("MOV AX, ").append(VALOR_ASIGNACION_16).append(System.lineSeparator())
                .append("MOV [ECX], AX\n")
                .append("JMP R_out\n")
                .append("R_asignacion32:\n")
                .append("MOV EAX, ").append(VALOR_ASIGNACION_32).append(System.lineSeparator())
                .append("MOV [ECX], EAX\n")
                .append("R_out:\n")
                .append("SUB ").append(INDICE).append(", 1\n")
                .append("JMP r_while\n")
                .append("r_end:\n")
                .append("MOV EAX, ").append(TEMP_EAX).append(System.lineSeparator())
                .append("MOV EBX, ").append(TEMP_EBX).append(System.lineSeparator())
                .append("MOV ECX, ").append(TEMP_ECX).append(System.lineSeparator())
                .append("RET\n\n");

        writer.append("start:");
        writer.append(System.lineSeparator()).append(System.lineSeparator());

        //Setear tamaño arreglos
        writer.append(";Seteamos los tamaños de las colecciones. \n");
        for (String lex : SymbolTable.keys()) {
            if(SymbolTable.getLex(lex).getAttr("use")!= null && SymbolTable.getLex(lex).getAttr("use").equals("COLECCION")){
                writer.append("MOV ["+ lex + "], "+SymbolTable.getLex(lex).getAttr("size"));
                writer.append("\n");
            }
        }
        writer.append(";Fin de seteo de tamaños. \n\n");

        for (Terceto t : AdminTercetos.list()) {
            writer.append(getCode(t));
        }


        writer.append("invoke ExitProcess, 0");
        writer.append(System.lineSeparator()).append(System.lineSeparator());
        writer.append("end start");

        writer.close();

        fileOutput.close();

    }

    private static String getCode(Terceto t) {

        StringBuilder instructions = new StringBuilder();
        AdminRegistros ar = AdminRegistros.getInstance();

        String reg_A = "";
        String reg_B = "";
        String reg1 = ""; //usado para colecciones
        String reg2 = ""; //usado para colecciones

        String tipo_op1 = use(t.getOperando1());
        String tipo_op2 = use(t.getOperando2());

        int size;

        if (t.type().equals("ULONG"))
            size = 32;
        else
            size = 16;

        switch (t.getOperacion()) {
            case "+":
                if(tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, size, instructions);
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegAD(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro o coleccion... opero sobre el segundo operando
                        if(tipo_op2.equals("coleccion")){
                            reg_A = reg2;
                        }else{
                            reg_A = getOP(t.getOperando2());
                        }

                        reg_B = getOP(t.getOperando1());
                    }
                } else { //primer operando registro o coleccion no importa lo q sea el segundo.. opero sobre el primero
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }
                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                    if(tipo_op2.equals("terceto") || tipo_op2.equals("coleccion")){//si ambos son terceto o coleccion libero al segundo
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
                if(tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, size, instructions);
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro o coleccion...  ocupo nuevo reg, genero codigo sobre ese reg y libero el otro
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        if(tipo_op2.equals("coleccion")){
                            reg_B = reg2;
                        }else{
                            reg_B = getOP(t.getOperando2());
                        }
                        ar.free(reg_B);
                    }
                } else { //primer operando registro o coleccion  no importa lo q sea el segundo.. opero sobre el primero
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }

                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                    if(tipo_op2.equals("terceto") || tipo_op2.equals("coleccion")){//si ambos son terceto o coleccion libero al segundo
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
                if(tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, size, instructions);
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

//                //llevo a variables auxiliares EAX y EDX (por si estaban ocupados)
//                instructions.append("MOV @tempEAX, EAX")
//                            .append("\n")
//                            .append("MOV @tempEDX, EDX")
//                            .append("\n")
//                            .append("MOV EAX, 0")
//                            .append("\n")
//                            .append("MOV EDX, 0")
//                            .append("\n");
//                ar.free("EAX");
//                ar.free("EDX");

                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegAD(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro o coleccion...  ocupo nuevo reg, genero codigo sobre ese reg y libero el otro
                        reg_A = ar.getRegBC(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        if(tipo_op2.equals("coleccion")){
                            reg_B = reg2;
                        }else{
                            reg_B = getOP(t.getOperando2());
                        }
                        ar.free(reg_B);
                    }
                } else { //primer operando registro o coleccion  no importa lo q sea el segundo.. opero sobre el primero
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }

                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                    if(tipo_op2.equals("terceto") || tipo_op2.equals("coleccion")){//si ambos son terceto o coleccion libero al segundo
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
                if(tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, size, instructions);
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

                if(tipo_op1.equals("variable")){ //primer operando variable
                    if(tipo_op2.equals("variable")) { //segundo operando variable
                        reg_A = ar.getRegAD(size);
                        instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ")
                                .append(getOP(t.getOperando1()))
                                .append("\n");
                        reg_B = getOP(t.getOperando2());
                    }else { //segundo operando registro o coleccion... opero sobre el segundo operando
                        if(tipo_op2.equals("coleccion")){
                            reg_A = reg2;
                        }else{
                            reg_A = getOP(t.getOperando2());
                        }

                        reg_B = getOP(t.getOperando1());
                    }
                } else { //primer operando registro o coleccion no importa lo q sea el segundo.. opero sobre el primero
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }
                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                    if(tipo_op2.equals("terceto") || tipo_op2.equals("coleccion")){//si ambos son terceto o coleccion libero al segundo
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
                if(tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, size, instructions);
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

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
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }
                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                    if(tipo_op1.equals("terceto") || tipo_op1.equals("coleccion")){
                        ar.free(reg_A);
                    }
                    if(tipo_op2.equals("terceto") || tipo_op1.equals("coleccion")){
                        ar.free(reg_B);
                    }
                }
                instructions.append("CMP " + reg_A + ", " + reg_B)
                            .append("\n");
                break;
            case ":=":
                if (tipo_op2.equals("coleccion"))
                    reg2 = elemento_coleccion(t.getOperando2(), ar, size, instructions);

                if(tipo_op2.equals("variable")) { //valor a asignar en variable
                    reg_B = ar.getRegBC(size);
                    instructions
                        .append("MOV ")
                        .append(reg_B)
                        .append(", ")
                        .append(getOP(t.getOperando2()))
                        .append("\n");
                }else{  //valor a asignar en registro o coleccion
                    if(tipo_op2.equals("coleccion")){
                        reg_B = reg2;
                    }else{
                        reg_B = getOP(t.getOperando2());
                    }
                }

                //lado izquierdo variable o coleccion
                if(tipo_op1.equals("coleccion")){ // elemento de una coleccion (coleccion[indice])
                    //calcular offset y asignar
                    reg_A = ar.getRegAD(32);
                    instructions.append("LEA ")
                            .append(reg_A)
                            .append(", ")
                            .append("[" + getOP(t.getOperando1()) + "]")
                            .append(System.lineSeparator())
                            .append("MOV ").append(COLECCION).append(", ")
                            .append(reg_A)
                            .append(System.lineSeparator())
                            .append("MOV ").append(TIPO).append(", ")
                            .append(size / 8)
                            .append("\n");
                    instructions.append("CALL _offset \n");
                    instructions.append("MOV ")
                                .append(reg_A)
                                .append(", ").append(OFFSET)
                                .append(System.lineSeparator());
                    instructions.append("MOV ")
                                .append("[")
                                .append(reg_A)
                                .append("], ")
                                .append(reg_B)
                                .append("\n");
                    ar.free(reg_A);
                }else{ // variable o rowing de coleccion
                    if (!SymbolTable.getLex(t.getOperando1()).getAttr("use").equals("COLECCION")) {//variable
                        instructions.append("MOV ")
                                .append(getOP(t.getOperando1()))
                                .append(", ")
                                .append(reg_B)
                                .append("\n");
                    }else{//rowing
                        if (size == 16){
                            instructions.append("MOV ").append(VALOR_ASIGNACION_16).append(", "+reg_B+System.lineSeparator());
                        }else{
                            instructions.append("MOV ").append(VALOR_ASIGNACION_32).append(", "+reg_B+System.lineSeparator());
                        }
                        reg_A = ar.getRegAD(32);
                        instructions.append("LEA ")
                                .append(reg_A)
                                .append(", ")
                                .append("[" + getOP(t.getOperando1()) + "]")
                                .append(System.lineSeparator())
                                .append("MOV ").append(COLECCION).append(", ")
                                .append(reg_A)
                                .append(System.lineSeparator())
                                .append("MOV ").append(TIPO).append(", ")
                                .append(size / 8)
                                .append("\n");
                        instructions.append("CALL _rowing\n");
                        ar.free(reg_A);
                    }
                }
                ar.free(reg_B);
                break;
            case "PRINT":
                Token token = SymbolTable.getLex(t.getOperando1());
                if(!token.getDescription().equals("CADENA")){
                    if(!token.getAttr("use").equals("COLECCION")){
                        instructions.append("invoke printf, cfm$(\"%. %d\\n\"), "+ getOP(t.getOperando1()))
                                    .append("\n");
                    }else {//TODO print coleccion for each
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
                if (tipo_op1.equals("coleccion"))
                    reg1 = elemento_coleccion(t.getOperando1(), ar, 16, instructions);
                if(tipo_op1.equals("variable")){ // si es variable la traigo a reg
                    reg_A = ar.getRegBC(16);
                    instructions.append("MOV ")
                            .append(reg_A)
                            .append(", ")
                            .append(getOP(t.getOperando1()))
                            .append("\n");
                }else{
                    if(tipo_op1.equals("coleccion")){
                        reg_A = reg1;
                    }else{
                        reg_A = getOP(t.getOperando1());
                    }
                }
                //Chequeo perdida de informacion
                instructions.append("CMP ")
                        .append(reg_A)
                        .append(", ")
                        .append("0")
                        .append("\n");
                instructions.append("JL error_negativo")
                        .append("\n");
                //si no es negativo realizo la conversion

                reg_B = ar.getRegAD(32);
                instructions.append("MOV ")
                        .append(reg_B)
                        .append(", 0")
                        .append("\n")
                        .append("MOV ")
                        .append(reg_B.substring(1))
                        .append(", ")
                        .append(reg_A)
                        .append("\n");

                ar.free(reg_A);
                t.setRegister(reg_B);
                break;

            case "call":
                reg_A = ar.getRegAD(32);
                switch (t.getOperando1()) {
                    case "_first":
                        instructions.append("LEA ")
                            .append(reg_A)
                            .append(", ")
                            .append("[" + getOP(t.getOperando2()) + "]")
                            .append(System.lineSeparator())
                            .append("MOV ").append(COLECCION).append(", ")
                            .append(reg_A)
                            .append(System.lineSeparator())
                            .append("MOV ").append(TIPO).append(", ")
                            .append(size / 8)
                            .append("\n");
                        instructions.append("CALL _first\n");
                        break;
                    case "_length":
                        instructions.append("LEA ")
                                .append(reg_A)
                                .append(", ")
                                .append("[" + getOP(t.getOperando2()) + "]")
                                .append(System.lineSeparator())
                                .append("MOV ").append(COLECCION).append(", ")
                                .append(reg_A)
                                .append("\n");
                        instructions.append("CALL _length\n");
                        break;
                    case "_last":
                        instructions.append("LEA ")
                                .append(reg_A)
                                .append(", ")
                                .append("[" + getOP(t.getOperando2()) + "]")
                                .append(System.lineSeparator())
                                .append("MOV ").append(COLECCION).append(", ")
                                .append(reg_A)
                                .append(System.lineSeparator())
                                .append("MOV ").append(TIPO).append(", ")
                                .append(size / 8)
                                .append("\n");
                        instructions.append("CALL _last\n");
                        break;
                }

                if (size == 16){
                    reg_A = reg_A.substring(1);
                    instructions.append("MOV ")
                      .append(reg_A)
                      .append(", ")
                      .append(RESULTADO_16)
                      .append(System.lineSeparator());
                }else{
                    
                    instructions.append("MOV ")
                      .append(reg_A)
                      .append(", ")
                      .append(RESULTADO_32)
                      .append(System.lineSeparator());
                }

                t.setRegister(reg_A);
                break;
        }

        return instructions.toString();
    }

    private static String elemento_coleccion(String op, AdminRegistros ar, int size, StringBuilder instructions){
        String reg_A = ar.getRegAD(32);
        instructions.append("LEA ")
                .append(reg_A)
                .append(", ")
                .append("[" + getOP(op) + "]")
                .append(System.lineSeparator())
                .append("MOV ").append(COLECCION).append(", ")
                .append(reg_A)
                .append(System.lineSeparator())
                .append("MOV ").append(TIPO).append(", ")
                .append(size / 8)
                .append(System.lineSeparator())
                .append("MOV ").append(INDICE).append(", "+op.substring(op.lastIndexOf("[")+1,op.lastIndexOf("]")))
                .append(System.lineSeparator());

        instructions.append("CALL _element").append("\n");

        if (size == 16){
            reg_A = reg_A.substring(1);
            instructions.append("MOV "+reg_A+", ").append(RESULTADO_16).append(System.lineSeparator());
        }else{
            instructions.append("MOV "+reg_A+", ").append(RESULTADO_32).append(System.lineSeparator());
        }
        return reg_A;
    }

    private static String use(String op) {
        if (op != null) {
            if (op.startsWith("["))
                return "terceto";
            else if(op.endsWith("]")){
                return "coleccion";
            }
            return "variable"; // return SymbolTable.getLex(op).getAttr("use");
        }
        return "";

    }
}
