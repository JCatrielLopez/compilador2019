public class AS3 implements SemanticAction {


    // Constantes enteras: Verifica el limite superior y lo agrega  a la Tabla de Simbolos.
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.returnChar(last_char);

        long cte;
        try {
            cte = Long.parseLong(lex.toString());
        } catch (java.lang.NumberFormatException e) {
            cte = (long) (Math.pow(2, 32) - 1);
        }

        if ((cte > Math.pow(2, 32)) && verbose) {
            Printer.print(String.format("%5s %s %5s %s %30s %s %10s", source.getLineNumber(), "|", " ", "|",
                    "ERROR Constant out of range.", "|", " "), Color.RED);
            cte = (long) (Math.pow(2, 32) - 1);

            //TODO Que hacemos cuando se detecta una cte fuera de rango? Descartamos la linea?
        } else {
            Token token = new Token(SymbolTable.getID("cte"), Long.toString(cte), "CONSTANT");
            SymbolTable.add(token);

            //TODO Contador de referencias ?

            return token;
        }
        return null;
    }
}
