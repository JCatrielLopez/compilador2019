public class AnalizadorLexico {

    private static final int TOKEN_INVALIDO = -1;
    private static final int ESTADO_INICIAL = 0;
    private static final int ESTADO_FINAL = 10;

    private BufferLectura programaFuente;

    private int[][] matrizEstados = {
            {1, 2, 10, 3, 10, 4, 5, 4, 10, 6, 9, 0, 0, 10, 10},
            {1, 1, 1, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {10, 2, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {10, 10, 10, 10, 7, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10},
            {7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 10},
            {7, 7, 7, 7, 8, 7, 7, 7, 7, 0, 7, 7, 7, 7, 10},
            {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 9, 10, 9, 10}
    };

    private  AccionSemantica[][] matrizAccionesSemanticas;

    public AnalizadorLexico(StringBuffer pf) {
        this.programaFuente = new BufferLectura(pf);
        this.cargarAS();
    }

    private void cargarAS() {
        matrizAccionesSemanticas = new AccionSemantica[9][15];
        AccionSemantica AS1 = new AS1();
        AccionSemantica AS2 = new AS3();
        AccionSemantica AS3 = new AS2();
        AccionSemantica AS4 = new AS4();
        AccionSemantica AS5 = new AS5();
        AccionSemantica AS6 = new AS6();
        AccionSemantica AS7 = new AS7();
        AccionSemantica AS8 = new AS8();
        AccionSemantica AS9 = new AS9();

        //TODO COMPLETAR
        matrizAccionesSemanticas[0][0] = AS1;
        matrizAccionesSemanticas[0][1] = AS1;
        matrizAccionesSemanticas[0][2] = AS5;
        matrizAccionesSemanticas[0][3] = AS1;
        matrizAccionesSemanticas[0][4] = AS4;
        matrizAccionesSemanticas[0][5] = AS1;
        matrizAccionesSemanticas[0][6] = AS1;
        matrizAccionesSemanticas[0][7] = AS1;
        matrizAccionesSemanticas[0][8] = AS4;
        matrizAccionesSemanticas[0][9] = AS1;
        matrizAccionesSemanticas[0][10] = AS8;
        matrizAccionesSemanticas[0][11] = AS8;
        matrizAccionesSemanticas[0][12] = AS9;
        matrizAccionesSemanticas[0][13] = AS5;
        matrizAccionesSemanticas[0][14] = AS8;

        matrizAccionesSemanticas[1][0] = AS1;
        matrizAccionesSemanticas[1][1] = AS1;
        matrizAccionesSemanticas[1][2] = AS1;
        matrizAccionesSemanticas[1][3] = AS2;
        matrizAccionesSemanticas[1][4] = AS2;
        matrizAccionesSemanticas[1][5] = AS2;
        matrizAccionesSemanticas[1][6] = AS2;
        matrizAccionesSemanticas[1][7] = AS2;
        matrizAccionesSemanticas[1][8] = AS2;
        matrizAccionesSemanticas[1][9] = AS2;
        matrizAccionesSemanticas[1][10] = AS2;
        matrizAccionesSemanticas[1][11] = AS2;
        matrizAccionesSemanticas[1][12] = AS2;
        matrizAccionesSemanticas[1][13] = AS2;
        matrizAccionesSemanticas[1][14] = AS2;

        matrizAccionesSemanticas[2][0] = AS3;
        matrizAccionesSemanticas[2][1] = AS1;
        matrizAccionesSemanticas[2][2] = AS3;
        matrizAccionesSemanticas[2][3] = AS3;
        matrizAccionesSemanticas[2][4] = AS3;
        matrizAccionesSemanticas[2][5] = AS3;
        matrizAccionesSemanticas[2][6] = AS3;
        matrizAccionesSemanticas[2][7] = AS3;
        matrizAccionesSemanticas[2][8] = AS3;
        matrizAccionesSemanticas[2][9] = AS3;
        matrizAccionesSemanticas[2][10] = AS3;
        matrizAccionesSemanticas[2][11] = AS3;
        matrizAccionesSemanticas[2][12] = AS3;
        matrizAccionesSemanticas[2][13] = AS3;
        matrizAccionesSemanticas[2][14] = AS3;

        matrizAccionesSemanticas[3][0] = AS5;
        matrizAccionesSemanticas[3][1] = AS5;
        matrizAccionesSemanticas[3][2] = AS5;
        matrizAccionesSemanticas[3][3] = AS5;
        matrizAccionesSemanticas[3][4] = AS5;
        matrizAccionesSemanticas[3][5] = AS4;
        matrizAccionesSemanticas[3][6] = AS5;
        matrizAccionesSemanticas[3][7] = AS5;
        matrizAccionesSemanticas[3][8] = AS5;
        matrizAccionesSemanticas[3][9] = AS5;
        matrizAccionesSemanticas[3][10] = AS5;
        matrizAccionesSemanticas[3][11] = AS5;
        matrizAccionesSemanticas[3][12] = AS5;
        matrizAccionesSemanticas[3][13] = AS5;
        matrizAccionesSemanticas[3][14] = AS5;

        matrizAccionesSemanticas[4][0] = AS6;
        matrizAccionesSemanticas[4][1] = AS6;
        matrizAccionesSemanticas[4][2] = AS6;
        matrizAccionesSemanticas[4][3] = AS6;
        matrizAccionesSemanticas[4][4] = AS6;
        matrizAccionesSemanticas[4][5] = AS4;
        matrizAccionesSemanticas[4][6] = AS6;
        matrizAccionesSemanticas[4][7] = AS6;
        matrizAccionesSemanticas[4][8] = AS6;
        matrizAccionesSemanticas[4][9] = AS6;
        matrizAccionesSemanticas[4][10] = AS6;
        matrizAccionesSemanticas[4][11] = AS6;
        matrizAccionesSemanticas[4][12] = AS6;
        matrizAccionesSemanticas[4][13] = AS6;
        matrizAccionesSemanticas[4][14] = AS6;

        matrizAccionesSemanticas[5][0] = AS6;
        matrizAccionesSemanticas[5][1] = AS6;
        matrizAccionesSemanticas[5][2] = AS6;
        matrizAccionesSemanticas[5][3] = AS6;
        matrizAccionesSemanticas[5][4] = AS6;
        matrizAccionesSemanticas[5][5] = AS4;
        matrizAccionesSemanticas[5][6] = AS6;
        matrizAccionesSemanticas[5][7] = AS4;
        matrizAccionesSemanticas[5][8] = AS6;
        matrizAccionesSemanticas[5][9] = AS6;
        matrizAccionesSemanticas[5][10] = AS6;
        matrizAccionesSemanticas[5][11] = AS6;
        matrizAccionesSemanticas[5][12] = AS6;
        matrizAccionesSemanticas[5][13] = AS6;
        matrizAccionesSemanticas[5][14] = AS6;

        matrizAccionesSemanticas[6][0] = AS6;
        matrizAccionesSemanticas[6][1] = AS6;
        matrizAccionesSemanticas[6][2] = AS6;
        matrizAccionesSemanticas[6][3] = AS6;
        matrizAccionesSemanticas[6][4] = AS8;
        matrizAccionesSemanticas[6][5] = AS6;
        matrizAccionesSemanticas[6][6] = AS6;
        matrizAccionesSemanticas[6][7] = AS6;
        matrizAccionesSemanticas[6][8] = AS6;
        matrizAccionesSemanticas[6][9] = AS6;
        matrizAccionesSemanticas[6][10] = AS6;
        matrizAccionesSemanticas[6][11] = AS6;
        matrizAccionesSemanticas[6][12] = AS6;
        matrizAccionesSemanticas[6][13] = AS6;
        matrizAccionesSemanticas[6][14] = AS6;

        matrizAccionesSemanticas[7][0] = AS8;
        matrizAccionesSemanticas[7][1] = AS8;
        matrizAccionesSemanticas[7][2] = AS8;
        matrizAccionesSemanticas[7][3] = AS8;
        matrizAccionesSemanticas[7][4] = AS8;
        matrizAccionesSemanticas[7][5] = AS8;
        matrizAccionesSemanticas[7][6] = AS8;
        matrizAccionesSemanticas[7][7] = AS8;
        matrizAccionesSemanticas[7][8] = AS8;
        matrizAccionesSemanticas[7][9] = AS8;
        matrizAccionesSemanticas[7][10] = AS8;
        matrizAccionesSemanticas[7][11] = AS8;
        matrizAccionesSemanticas[7][12] = AS8;
        matrizAccionesSemanticas[7][13] = AS8;
        matrizAccionesSemanticas[7][14] = AS5;

        matrizAccionesSemanticas[8][0] = AS8;
        matrizAccionesSemanticas[8][1] = AS8;
        matrizAccionesSemanticas[8][2] = AS8;
        matrizAccionesSemanticas[8][3] = AS8;
        matrizAccionesSemanticas[8][4] = AS8;
        matrizAccionesSemanticas[8][5] = AS8;
        matrizAccionesSemanticas[8][6] = AS8;
        matrizAccionesSemanticas[8][7] = AS8;
        matrizAccionesSemanticas[8][8] = AS8;
        matrizAccionesSemanticas[8][9] = AS8;
        matrizAccionesSemanticas[8][10] = AS8;
        matrizAccionesSemanticas[8][11] = AS8;
        matrizAccionesSemanticas[8][12] = AS8;
        matrizAccionesSemanticas[8][13] = AS8;
        matrizAccionesSemanticas[8][14] = AS5;

        matrizAccionesSemanticas[9][0] = AS1;
        matrizAccionesSemanticas[9][1] = AS1;
        matrizAccionesSemanticas[9][2] = AS1;
        matrizAccionesSemanticas[9][3] = AS1;
        matrizAccionesSemanticas[9][4] = AS1;
        matrizAccionesSemanticas[9][5] = AS1;
        matrizAccionesSemanticas[9][6] = AS1;
        matrizAccionesSemanticas[9][7] = AS1;
        matrizAccionesSemanticas[9][8] = AS1;
        matrizAccionesSemanticas[9][9] = AS1;
        matrizAccionesSemanticas[9][10] = AS7;
        matrizAccionesSemanticas[9][11] = AS1;
        matrizAccionesSemanticas[9][12] = AS5;
        matrizAccionesSemanticas[9][13] = AS1;
        matrizAccionesSemanticas[9][14] = AS5;
    }

    private int getSimbolo(int caracter) {


        if ((caracter >= 65 && caracter <= 90) ||  (caracter >= 97 && caracter <= 122)) // [a-z] [A-Z]
            return 0;
        else if (caracter >= 48 && caracter <= 57) // [0-9]
            return 1;
        else if (caracter == 43) // +
            return 4;
        else if (caracter == 58) // :
            return 3;
        else if (caracter == 45 || caracter == 42 ||
                caracter == 40 || caracter == 41 ||
                caracter == 44 || caracter == 59 || caracter == 91 || caracter == 93) // - * ( ) , ;
            return 8;
        else if (caracter == 61) // =
            return 5;
        else if (caracter == 60) // <
            return 6;
        else if (caracter == 62) // >
            return 7;
        else if (caracter == 47) // /
            return 9;
        else if (caracter == 37) // %
            return 10;
        else if (caracter == 95) // _
            return 2;
        else if (caracter == 9 || caracter == 32) // \t \s
            return 11;
        else if (caracter == 10) // \n
            return 12;
        else if (caracter == 0) // eof
            return 14;
        else // otro
            return 13;

    }

    public boolean notEOF() {
        return !programaFuente.eof();
    }

    public int getNroLinea() {
        return programaFuente.getNroLinea();
    }

    public int yylex() {
        StringBuilder lexema = new StringBuilder();

        int estado = ESTADO_INICIAL;
        Token devolucion = null;
        while (estado != ESTADO_FINAL) {

            int entrada = 0; // caracter nulo
            if(notEOF()) {
                entrada = programaFuente.getCaracter();
            }
            AccionSemantica as = matrizAccionesSemanticas[estado][getSimbolo(entrada)];
            devolucion = as.ejecutar(programaFuente, lexema, (char) entrada);
            estado = matrizEstados[estado][getSimbolo(entrada)];
        }

//        if (devolucion != null) {
//            Parser.yylval = new ParserVal(devolucion.getLexema());
//            System.out.println("Linea " + programaFuente.getNroLinea() + ": (AL) " + devolucion.imprimir());
//            return devolucion.getIdentificador();
//        }

        System.out.println("Linea " + programaFuente.getNroLinea() + ": (AL) " + devolucion);

        return TOKEN_INVALIDO; // error
    }

}
