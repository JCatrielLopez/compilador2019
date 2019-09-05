public class AS6 implements AccionSemantica {

    // Literales
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {

        programaFuente.devolverCaracter(ultimo_caracter);
        Token token = new Token(TablaSimbolos.getID(lexema.toString()), lexema.toString(), "Literal");

        return token;
    }
}
