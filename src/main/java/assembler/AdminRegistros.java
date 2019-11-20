package assembler;

import java.util.HashMap;

public class AdminRegistros {

    // SINGLETON
    private static AdminRegistros instance;

    public static AdminRegistros getInstance(){
        if( AdminRegistros.instance == null){
            AdminRegistros.instance = new AdminRegistros();
        }
        return AdminRegistros.instance;
    }

    // DINAMICOS
    // registers = {register: [True, False]}
    //                         True: El registro esta liberado.
    //                         False: El registro esta ocupado.
    private HashMap<String, Boolean> registers = new HashMap<String, Boolean>();


    public AdminRegistros() {
        registers.put("BX", true);
        registers.put("CX", true);
        registers.put("AX", true);
        registers.put("DX", true);
    }

    public void free(String register) {
        if (register.startsWith("E")) // Borro la E (EBX -> BX)
            register = register.substring(1);

        if (this.registers.containsKey(register))
            this.registers.put(register, true);
    }

    public String getRegAD(Integer bits) {
        String register = null;

        if(registers.get("AX")){
            register = "AX";
        }else if (registers.get("DX")){
            register = "DX";
        }
        if (bits == 32)
            register = "E" + register;

        registers.put(register, false);
        return register;
    }

    public String getRegBC(Integer bits) {
        String register = null;
        if(registers.get("BX")){
            register = "BX";
        }else if (registers.get("CX")){
            register = "CX";
        }
        if (bits == 32)
            register = "E" + register;
        registers.put(register, false);
        return register;
    }

    public boolean is_aviable(String register){return registers.get(register);}

    public void occupy(String reg) {
        this.registers.put(reg, false);
    }
}
