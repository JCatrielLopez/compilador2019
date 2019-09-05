public class AS4 implements AccionSemantica{

    // Agregar caracter y retornar token
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {

        lexema.append(ultimo_caracter);
        Token token = new Token(TablaSimbolos.getID(lexema.toString()), lexema.toString(), "Literal");

        return token;
    }
}
