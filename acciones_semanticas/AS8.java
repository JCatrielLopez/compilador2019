public class AS8 implements AccionSemantica {
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {

        if (ultimo_caracter == 0)
            System.out.printf("Fin de archivo.");

        return null;
    }
}
