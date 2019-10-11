class Lexer {

    private static final int INVALID_TOKEN = -1;
    private static final int START = 0;
    private static final int END = 10;

    private final Buffer source;

    private final int[][] states = {
            {1, 2, 10, 3, 10, 3, 5, 4, 10, 6, 9, 0, 0, 10, 10},
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

    private SemanticAction[][] actions;
    private final boolean verbose;

    public Lexer(StringBuffer pf, boolean verbose) {
        this.source = new Buffer(pf);
        this.declare_semantic_actions();
        this.verbose = verbose;
    }

    private void declare_semantic_actions() {

        actions = new SemanticAction[10][15];

        SemanticAction AS1 = new AS1();
        SemanticAction AS2 = new AS2();
        SemanticAction AS3 = new AS3();
        SemanticAction AS4 = new AS4();
        SemanticAction AS5 = new AS5();
        SemanticAction AS6 = new AS6();
        SemanticAction AS7 = new AS7();
        SemanticAction AS8 = new AS8();
        SemanticAction AS9 = new AS9();
        SemanticAction AS10 = new AS10();

        actions[0][0] = AS1;
        actions[0][1] = AS1;
        actions[0][2] = AS5;
        actions[0][3] = AS1;
        actions[0][4] = AS4;
        actions[0][5] = AS1;
        actions[0][6] = AS1;
        actions[0][7] = AS1;
        actions[0][8] = AS4;
        actions[0][9] = AS1;
        actions[0][10] = AS8;
        actions[0][11] = AS8;
        actions[0][12] = AS9;
        actions[0][13] = AS5;
        actions[0][14] = AS8;

        actions[1][0] = AS1;
        actions[1][1] = AS1;
        actions[1][2] = AS1;
        actions[1][3] = AS2;
        actions[1][4] = AS2;
        actions[1][5] = AS2;
        actions[1][6] = AS2;
        actions[1][7] = AS2;
        actions[1][8] = AS2;
        actions[1][9] = AS2;
        actions[1][10] = AS2;
        actions[1][11] = AS2;
        actions[1][12] = AS2;
        actions[1][13] = AS2;
        actions[1][14] = AS2;

        actions[2][0] = AS3;
        actions[2][1] = AS1;
        actions[2][2] = AS3;
        actions[2][3] = AS3;
        actions[2][4] = AS3;
        actions[2][5] = AS3;
        actions[2][6] = AS3;
        actions[2][7] = AS3;
        actions[2][8] = AS3;
        actions[2][9] = AS3;
        actions[2][10] = AS3;
        actions[2][11] = AS3;
        actions[2][12] = AS3;
        actions[2][13] = AS3;
        actions[2][14] = AS3;

        actions[3][0] = AS10;
        actions[3][1] = AS10;
        actions[3][2] = AS10;
        actions[3][3] = AS10;
        actions[3][4] = AS10;
        actions[3][5] = AS4;
        actions[3][6] = AS10;
        actions[3][7] = AS10;
        actions[3][8] = AS10;
        actions[3][9] = AS10;
        actions[3][10] = AS10;
        actions[3][11] = AS10;
        actions[3][12] = AS10;
        actions[3][13] = AS10;
        actions[3][14] = AS10;

        actions[4][0] = AS6;
        actions[4][1] = AS6;
        actions[4][2] = AS6;
        actions[4][3] = AS6;
        actions[4][4] = AS6;
        actions[4][5] = AS4;
        actions[4][6] = AS6;
        actions[4][7] = AS6;
        actions[4][8] = AS6;
        actions[4][9] = AS6;
        actions[4][10] = AS6;
        actions[4][11] = AS6;
        actions[4][12] = AS6;
        actions[4][13] = AS6;
        actions[4][14] = AS6;

        actions[5][0] = AS6;
        actions[5][1] = AS6;
        actions[5][2] = AS6;
        actions[5][3] = AS6;
        actions[5][4] = AS6;
        actions[5][5] = AS4;
        actions[5][6] = AS6;
        actions[5][7] = AS4;
        actions[5][8] = AS6;
        actions[5][9] = AS6;
        actions[5][10] = AS6;
        actions[5][11] = AS6;
        actions[5][12] = AS6;
        actions[5][13] = AS6;
        actions[5][14] = AS6;

        actions[6][0] = AS6;
        actions[6][1] = AS6;
        actions[6][2] = AS6;
        actions[6][3] = AS6;
        actions[6][4] = AS8;
        actions[6][5] = AS6;
        actions[6][6] = AS6;
        actions[6][7] = AS6;
        actions[6][8] = AS6;
        actions[6][9] = AS6;
        actions[6][10] = AS6;
        actions[6][11] = AS6;
        actions[6][12] = AS6;
        actions[6][13] = AS6;
        actions[6][14] = AS6;

        actions[7][0] = AS8;
        actions[7][1] = AS8;
        actions[7][2] = AS8;
        actions[7][3] = AS8;
        actions[7][4] = AS8;
        actions[7][5] = AS8;
        actions[7][6] = AS8;
        actions[7][7] = AS8;
        actions[7][8] = AS8;
        actions[7][9] = AS8;
        actions[7][10] = AS8;
        actions[7][11] = AS8;
        actions[7][12] = AS9;
        actions[7][13] = AS8;
        actions[7][14] = AS5;

        actions[8][0] = AS8;
        actions[8][1] = AS8;
        actions[8][2] = AS8;
        actions[8][3] = AS8;
        actions[8][4] = AS8;
        actions[8][5] = AS8;
        actions[8][6] = AS8;
        actions[8][7] = AS8;
        actions[8][8] = AS8;
        actions[8][9] = AS8;
        actions[8][10] = AS8;
        actions[8][11] = AS8;
        actions[8][12] = AS9;
        actions[8][13] = AS8;
        actions[8][14] = AS5;

        actions[9][0] = AS1;
        actions[9][1] = AS1;
        actions[9][2] = AS1;
        actions[9][3] = AS1;
        actions[9][4] = AS1;
        actions[9][5] = AS1;
        actions[9][6] = AS1;
        actions[9][7] = AS1;
        actions[9][8] = AS1;
        actions[9][9] = AS1;
        actions[9][10] = AS7;
        actions[9][11] = AS1;
        actions[9][12] = AS5;
        actions[9][13] = AS1;
        actions[9][14] = AS5;
    }

    private int getColumn(int ascii_code) {


        if ((ascii_code >= 65 && ascii_code <= 90) || (ascii_code >= 97 && ascii_code <= 122)) // [a-z] [A-Z]
            return 0;
        else if (ascii_code >= 48 && ascii_code <= 57) // [0-9]
            return 1;
        else if (ascii_code == 43) // +
            return 4;
        else if (ascii_code == 58) // :
            return 3;
        else if (ascii_code == 45 || ascii_code == 42 ||
                ascii_code == 40 || ascii_code == 41 ||
                ascii_code == 44 || ascii_code == 59 || ascii_code == 91 || ascii_code == 93 || ascii_code == 46) // - * ( ) , ; .
            return 8;
        else if (ascii_code == 61) // =
            return 5;
        else if (ascii_code == 60) // <
            return 6;
        else if (ascii_code == 62) // >
            return 7;
        else if (ascii_code == 47) // /
            return 9;
        else if (ascii_code == 37) // %
            return 10;
        else if (ascii_code == 95) // _
            return 2;
        else if (ascii_code == 9 || ascii_code == 32) // \t \s
            return 11;
        else if (ascii_code == 10) // \n
            return 12;
        else if (ascii_code == 0) // eof
            return 14;
        else // otro
            return 13;

    }

    public boolean notEOF() {
        return !source.eof();
    }

    public int getLineNumber() {
        return source.getLineNumber();
    }

    public int yylex() {
        StringBuilder lex = new StringBuilder();


        int state = START;
        boolean EOF = false;
        Token token = null;

        while (state != END) {

            int new_char = 0; // EOF
            if (notEOF()) {
                new_char = source.getChar();
            } else
                EOF = true;

            SemanticAction as = actions[state][getColumn(new_char)];
            token = as.execute(source, lex, (char) new_char, this.verbose);
            state = states[state][getColumn(new_char)];

        }

        if (token != null) {
            if (this.verbose) {
                Printer.print(String.format("%5s %s %s", source.getLineNumber(), "|", token), Color.RESET);
            }
            Parser.yylval = new ParserVal(token.getLex());
            return token.getID();
        }

        return INVALID_TOKEN; // error
    }

}
