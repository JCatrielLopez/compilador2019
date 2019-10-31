package parser;

public class Terceto {

    static int cont = 0;
    String id;
    String operacion;
    String operando1;
    String operando2;

    public Terceto(String operacion, String operando1, String operando2) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = operando1;
        this.operando2 = operando2;
    }

    public Terceto(String operacion) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = null;
        this.operando2 = null;
    }

    public String getId() {
        return id;
    }

    public void setOperador(int pos, String operador) {
        if (pos == 1)
            if (this.operando1 == null) //Para no pisar tercetos ya definidos. Sacarlo si hace falta en el futuro.
                this.operando1 = operador;
            else if (this.operando2 == null)
                this.operando2 = operador;
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


}
