package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import org.hibernate.validator.NotNull;

public class AutomatonInfo {

    @NotNull
    private String automatonType;
    @NotNull
    private String resetValue;
    @NotNull
    private String clockValue;

    public String getAutomatonType() {
        return automatonType;
    }

    public void setAutomatonType(String automatonType) {
        this.automatonType = automatonType;
    }

    public String getResetValue() {
        return resetValue;
    }

    public void setResetValue(String resetValue) {
        this.resetValue = resetValue;
    }

    public String getClockValue() {
        return clockValue;
    }

    public void setClockValue(String clockValue) {
        this.clockValue = clockValue;
    }

}
