package semantic_actions;

import globals.Error;
import globals.SymbolTable;
import lexer.Buffer;
import lexer.Token;

public class AS3 implements SemanticAction {


    // Constantes enteras: Verifica el limite superior y lo agrega  a la Tabla de Simbolos.
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.returnChar(last_char);

        long cte;
        String type = "ULONG";
        try {
            cte = Long.parseLong(lex.toString());
        } catch (java.lang.NumberFormatException e) {
            Error.add(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "WARNING CTE fuera de rango, se le asigna el valor maximo."));
            cte = (long) (Math.pow(2, 32) - 1);
        }

        if ((cte > Math.pow(2, 32)-1)) {
            Error.add(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "WARNING CTE fuera de rango, se le asigna el valor maximo."));
            cte = (long) (Math.pow(2, 32) - 1);
        }

        if ((cte <= (Math.pow(2, 15)))) {
            type = "INT";
        }
        Token token = new Token(SymbolTable.getID("cte"), Long.toString(cte), "CONSTANT");
        token.addAttr("type",type);
        SymbolTable.add(token);


        Token t = SymbolTable.getLex(Long.toString(cte));
        if (t.getAttr("contador") == null) {
            t.addAttr("contador", "1");
        } else {
            int contador = Integer.parseInt(t.getAttr("contador")) + 1;
            t.addAttr("contador", String.valueOf(contador));
        }

        return token;
//        return null;
    }
}