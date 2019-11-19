package assembler;

public class Terceto {

    static int cont = 0;
    private String id;
    private String operacion;
    private String operando1;
    private String operando2;
    private String type;
    private String register;

    public Terceto(String operacion, String operando1, String operando2) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = operando1;
        this.operando2 = operando2;
        this.type = "";
        this.register = "";
    }

    public Terceto(String operacion, String operando1) {
        cont++;
        this.id = "[" + cont + "]";
        this.operacion = operacion;
        this.operando1 = operando1;
        this.operando2 = null;
        this.type = "";
        this.register = "";
    }

    public String getId() {
        return id;
    }

    public void setOperando2(String operador) {
        this.operando2 = operador;
    }
  
    public void setOperando1(String operador) {
        this.operando1 = operador;
    }

    public void setType(String type){
        this.type = type;
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
                //.append(", ")
                //.append(this.type)
                .append(")");

        return out.toString();
    }


    public String type() {
        return this.type;
    }

    public String getRegister() {
        return this.register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
