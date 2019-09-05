public class AS2 implements AccionSemantica {
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {
        programaFuente.devolverCaracter(ultimo_caracter);

        if (lexema.length() > 25){
            lexema = new StringBuilder(lexema.substring(0, 25));
        }

        Token token;

        if (!TablaSimbolos.contiene(lexema.toString())) {
            token = new Token(TablaSimbolos.getID("id"), lexema.toString(), "Identificador");
            TablaSimbolos.addSimbolo(token);
        }
        else{
            token = new Token(TablaSimbolos.getID(lexema.toString()), lexema.toString(), "Palabra Reservada");
        }

        return token;

    }
}
