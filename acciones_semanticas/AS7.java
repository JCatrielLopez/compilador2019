public class AS7 implements AccionSemantica {

    // Cadena de una linea
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {

        Token token = new Token(TablaSimbolos.getID(lexema.toString()), lexema.toString(), "Identificador");

        TablaSimbolos.addSimbolo(token);

        return token;

    }
}
