public class AS9 implements AccionSemantica {

    // Salto de linea
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {
        programaFuente.sigLinea();

        return null;
    }
}
