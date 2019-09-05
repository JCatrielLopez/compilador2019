public class BufferLectura {

    private StringBuffer buffer;
    private int nroLinea;

    public BufferLectura(StringBuffer sb) {
        this.buffer = sb;
        this.nroLinea = 1;
    }

    public boolean eof() {
        return buffer.length() == 0;
    }

    public int getCaracter() {
        int next = -1;
        if (! this.eof()) {
            next = buffer.charAt(0);
            buffer.deleteCharAt(0);
            if (next == 13) { //consume los \r
                next = buffer.charAt(0);
                buffer.deleteCharAt(0);
            }
        }
        return next;
    }

    public void devolverCaracter(int caracter) {
        if(caracter != 0) {
            buffer.insert(0, (char) caracter);
        }
    }

    public void sigLinea() {
        nroLinea++;
    }

    public int getNroLinea() {
        return nroLinea;
    }

}
