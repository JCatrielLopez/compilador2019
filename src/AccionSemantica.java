public interface AccionSemantica {
    Token ejecutar(BufferLectura programaFuente, StringBuilder lexema, char ultimo_caracter);
}
