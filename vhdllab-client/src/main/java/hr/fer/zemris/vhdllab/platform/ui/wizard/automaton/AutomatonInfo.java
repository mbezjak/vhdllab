package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

public class AutomatonInfo {

    private String automatonType;
    private String resetValue;
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
