public class AS3 implements AccionSemantica {


    // Constantes enteras: Verifica el limite superior y lo agrega  a la Tabla de Simbolos.
    @Override
    public Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter) {
        programaFuente.devolverCaracter(ultimo_caracter);

        long cte = Long.parseLong(lexema.toString());

        if (cte > Math.pow(2, 32)){
            System.out.printf("Linea " + programaFuente.getNroLinea() + "(AL) ERROR Constante fuera de rango.");
        }
        else{
            Token token = new Token(TablaSimbolos.getID("cte"), Long.toString(cte), "Constante");
            TablaSimbolos.addSimbolo(token);

            //TODO Contador de referencias

            return token;
        }
        return null;
    }
}
