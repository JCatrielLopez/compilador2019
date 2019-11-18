package assembler;

import java.util.HashMap;

public class AdminRegistros {

    private HashMap<String, Boolean> registers = new HashMap<String, Boolean>();


    public AdminRegistros() {
        registers.put("bx", false);
        registers.put("cx", false);
    }

    public void free(String register) {
        if (register.startsWith("E")) // Borro la E (EBX -> BX)
            register = register.substring(1);

        if (this.registers.containsKey(register))
            this.registers.put(register, false);
    }
}
