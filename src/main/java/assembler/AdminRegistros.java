package assembler;

import java.util.HashMap;

public class AdminRegistros {

    // registers = {register: [True, False]}
    //                         True: El registro esta liberado.
    //                         False: El registro esta ocupado.
    private HashMap<String, Boolean> registers = new HashMap<String, Boolean>();


    public AdminRegistros() {
        registers.put("bx", true);
        registers.put("cx", true);
        registers.put("ax", true);
        registers.put("dx", true);
        registers.put("ebx", true);
        registers.put("ecx", true);
        registers.put("eax", true);
        registers.put("edx", true);
    }

    public void free(String register) {
        if (register.startsWith("e")) // Borro la E (EBX -> BX)
            register = register.substring(1);

        if (this.registers.containsKey(register))
            this.registers.put(register, true);
    }

    public String available(Integer bits) {
        for (String register : registers.keySet()) {
            if (bits == 32)
                if (registers.get(register) && register.startsWith("e")) {
                    registers.put(register, false);
                    return register;
                } else if (registers.get(register) && !register.startsWith("e")) {
                    registers.put(register, false);
                    return register;
                }
        }
        return null;
    }

    public String getRegA(Integer bits) {
        if (bits == 32)
            return "eax";
        else
            return "ax";
    }

    public boolean is_available(String register) {
        return this.registers.get(register);
    }

    public String getRegD(Integer bits) {
        if (bits == 32)
            return "edx";
        else
            return "dx";
    }

    public void occupy(String reg) {
        this.registers.put(reg, false);
    }
}
