public class AS3 implements SemanticAction {


    // Constantes enteras: Verifica el limite superior y lo agrega  a la Tabla de Simbolos.
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.returnChar(last_char);

        long cte;
        try {
            cte = Long.parseLong(lex.toString());
        } catch (java.lang.NumberFormatException e) {
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "WARNING CTE fuera de rango, se le asigna el valor maximo."), Color.YELLOW);
            cte = (long) (Math.pow(2, 32) - 1);
        }

        if ((cte > Math.pow(2, 32))) {
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "WARNING CTE fuera de rango, se le asigna el valor maximo."), Color.YELLOW);
            cte = (long) (Math.pow(2, 32) - 1);

        }
        Token token = new Token(SymbolTable.getID("cte"), Long.toString(cte), "CONSTANT");
        SymbolTable.add(token);

        if (token.getAttr("contador") == null) {
            token.addAttr("contador", "1");
        } else {
            int contador = Integer.parseInt(token.getAttr("contador")) + 1;
            token.addAttr("contador", String.valueOf(contador));
        }

        return token;
//        return null;
    }
}