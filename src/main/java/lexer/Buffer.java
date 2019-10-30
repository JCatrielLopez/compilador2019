package lexer;

public class Buffer {

    private final StringBuffer buffer;
    private int lineNumber;

    public Buffer(StringBuffer sb) {
        this.buffer = sb;
        this.lineNumber = 1;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean eof() {
        return buffer.length() == 0;
    }

    public int getChar() {
        int next = -1;
        if (!this.eof()) {
            next = buffer.charAt(0);
            buffer.deleteCharAt(0);
            if (next == 13) { //consume los \r
                next = buffer.charAt(0);
                buffer.deleteCharAt(0);
            }
        }
        return next;
    }

    public void returnChar(int character) {
        if (character != 0) {
            buffer.insert(0, (char) character);
        }
    }

    public void nextLine() {
        lineNumber++;
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
