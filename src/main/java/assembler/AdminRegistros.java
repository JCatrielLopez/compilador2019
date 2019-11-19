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
    }

    public void free(String register) {
        if (register.startsWith("E")) // Borro la E (EBX -> BX)
            register = register.substring(1);

        if (this.registers.containsKey(register))
            this.registers.put(register, true);
    }

    public String available(Integer bits) {
        for (String register : registers.keySet())
            if (registers.get(register)) {
                registers.put(register, false);

                if (bits == 32)
                    return "E" + register;
                else
                    return register;
            }
        return null;
    }

    public String getRegA(Integer bits) {
        if (bits == 32)
            return "EAX";
        else
            return "AX";
    }

    public String getRegD(Integer bits) {
        if (bits == 32)
            return "EDX";
        else
            return "DX";
    }
}
