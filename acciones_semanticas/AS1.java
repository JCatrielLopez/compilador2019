public class AS1 implements AccionSemantica {
    @Override

    // Agregar caracter al lexema.
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {
        lexema.append(ultimo_caracter);

        return null;
    }
}
