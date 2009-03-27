package hr.fer.zemris.vhdllab.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.hibernate.validator.Validator;

public class NameFormatConstraintValidator implements
        Validator<NameFormatConstraint> {

    private static final String NAME_PATTERN = "\\p{Alpha}\\p{Alnum}*(_\\p{Alnum}+)*";
    private static final Pattern PATTERN = Pattern.compile(NAME_PATTERN);
    private static final List<String> ILLEGAL_VHDL_NAMES = new ArrayList<String>(
            110);

    static {
        // VHDL keywords
        add("abs");
        add("access");
        add("after");
        add("alias");
        add("all");
        add("and");
        add("architecture");
        add("array");
        add("assert");
        add("attribute");
        add("begin");
        add("block");
        add("body");
        add("buffer");
        add("bus");
        add("case");
        add("component");
        add("configuration");
        add("constant");
        add("disconnect");
        add("downto");
        add("else");
        add("elsif");
        add("end");
        add("entity");
        add("exit");
        add("file");
        add("for");
        add("function");
        add("generate");
        add("generic");
        add("group");
        add("guarded");
        add("if");
        add("impure");
        add("in");
        add("inertial");
        add("inout");
        add("is");
        add("label");
        add("library");
        add("linkage");
        add("literal");
        add("loop");
        add("map");
        add("mod");
        add("nand");
        add("new");
        add("next");
        add("nor");
        add("not");
        add("null");
        add("of");
        add("on");
        add("open");
        add("or");
        add("others");
        add("out");
        add("package");
        add("port");
        add("postponed");
        add("procedure");
        add("process");
        add("pure");
        add("range");
        add("record");
        add("register");
        add("reject");
        add("rem");
        add("report");
        add("return");
        add("rol");
        add("select");
        add("severity");
        add("signal");
        add("shared");
        add("sla");
        add("sll");
        add("sra");
        add("srl");
        add("subtype");
        add("then");
        add("to");
        add("transport");
        add("type");
        add("unaffected");
        add("units");
        add("until");
        add("use");
        add("variable");
        add("wait");
        add("when");
        add("while");
        add("with");
        add("xnor");
        add("xor");

        // other illegal names
        add("switch");
        add("std_logic");
        add("std_logic_vector");
        add("integer");
        add("string");
        add("double");
        add("float");
        add("bit");
        add("vector");
        add("standard");
        add("logic");
        add("vhdl");
        add("work");
    }

    private static void add(String name) {
        ILLEGAL_VHDL_NAMES.add(name.toLowerCase());
    }

    @Override
    public void initialize(NameFormatConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof String) {
            return isCorrectName((String) value);
        }
        return false;
    }

    private boolean isCorrectName(String name) {
        if (name == null)
            return true;
        return isCorretlyFormatted(name) && !isIllegalVHLDName(name);
    }

    private boolean isCorretlyFormatted(String name) {
        return PATTERN.matcher(name).matches();
    }

    private boolean isIllegalVHLDName(String name) {
        return ILLEGAL_VHDL_NAMES.contains(name.toLowerCase());
    }

}
