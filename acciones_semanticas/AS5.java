public class AS5 implements AccionSemantica {

    // Caracter invalido
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {

        programaFuente.devolverCaracter(ultimo_caracter);
        System.out.printf("Linea " + programaFuente.getNroLinea() + "(AL) ERROR Caracter " + lexema +  "invalido");
        lexema.setLength(0);

        return null;
    }
}
