package semantic_actions;

import globals.Color;
import globals.Printer;
import globals.SymbolTable;
import lexer.Buffer;
import lexer.Token;

public class AS2 implements SemanticAction {
    @Override
    public Token execute(Buffer source, StringBuilder lex, char last_char, boolean verbose) {
        source.returnChar(last_char);

        // Se trunca el identificador a un maximo de 25 caracteres.
        if (lex.length() > 25) {
            lex = new StringBuilder(lex.substring(0, 25));
            Printer.print(String.format("%5s %s %3s %s %s", source.getLineNumber(), "|", "AL", "|",
                    "WARNING Largo de ID fuera de rango, se lo trunca a 25 caracteres."), Color.YELLOW);
        }

        Token token;

        if (!SymbolTable.isReserved(lex.toString()) || lex.toString().equals("cadena")) {
            token = new Token(SymbolTable.getID("id"), lex.toString(), "ID");
            SymbolTable.add(token);
        } else {
            token = new Token(SymbolTable.getID(lex.toString()), lex.toString(), "RESERVED KEYWORD");
        }

        return token;

    }
}