package assembler;

public class Terceto {

    static int cont = 0;
    private String id;
    private String operacion;
    private String operando1;
    private String operando2;

    public Terceto(String operacion, String operando1, String operando2) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = operando1;
        this.operando2 = operando2;
    }

    public Terceto(String operacion, String operando1) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = operando1;
        this.operando2 = null;
    }

    public String getId() {
        return id;
    }

    public String getOperando1() {
        return operando1;
    }

    public String getOperando2() {
        return operando2;
    }

    public String getOperacion() {
        return operacion;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(this.id)
                .append(": ")
                .append("(")
                .append(this.operacion)
                .append(", ")
                .append(this.operando1)
                .append(", ")
                .append(this.operando2)
                .append(")");

        return out.toString();
    }


    public String type() { //TODO Agregar el tipo a los tercetos.
        return "";
    }

    public void setOperando1(String s) {
        this.operando1 = s;
    }

    public void setOperando2(String s) {
        this.operando2 = s;
    }
}
