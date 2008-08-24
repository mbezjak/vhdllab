package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.api.FileTypes;

import java.util.ArrayList;
import java.util.List;

public class FileTypesUtil {

    public static boolean isSimulation(String type) {
        return type.equals(FileTypes.VHDL_SIMULATION);
    }

    public static boolean isTestbench(String type) {
        return type.equals(FileTypes.VHDL_TESTBENCH);
    }

    public static boolean isCircuit(String type) {
        return type.equals(FileTypes.VHDL_SOURCE)
                || type.equals(FileTypes.VHDL_SCHEMA)
                || type.equals(FileTypes.VHDL_AUTOMATON)
                || type.equals(FileTypes.VHDL_PREDEFINED);
    }

    public static boolean isPredefined(String type) {
        return type.equals(FileTypes.VHDL_PREDEFINED);
    }

    public static boolean isPreferences(String type) {
        return type.equals(FileTypes.PREFERENCES_USER);
    }

    public static boolean isNotVHDL(String type) {
        return !(isSimulation(type) || isTestbench(type) || isCircuit(type));
    }

    public static List<String> values() {
        List<String> values = new ArrayList<String>();
        values.add(FileTypes.VHDL_SOURCE);
        values.add(FileTypes.VHDL_AUTOMATON);
        values.add(FileTypes.VHDL_SCHEMA);
        values.add(FileTypes.VHDL_TESTBENCH);
        values.add(FileTypes.VHDL_SIMULATION);
        values.add(FileTypes.VHDL_PREDEFINED);
        values.add(FileTypes.PREFERENCES_USER);
        return values;
    }

}
