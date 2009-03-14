package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.service.ci.PortDirection;
import hr.fer.zemris.vhdllab.service.ci.PortType;

public class CircuitInterfaceObject {

    private String name;
    private PortDirection portDirection;
    private PortType typeName;
    private Integer from;
    private Integer to;

    public CircuitInterfaceObject() {
        setPortDirection(PortDirection.IN);
        setTypeName(PortType.STD_LOGIC);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortDirection getPortDirection() {
        return portDirection;
    }

    public void setPortDirection(PortDirection portDirection) {
        this.portDirection = portDirection;
    }

    public PortType getTypeName() {
        return typeName;
    }

    public void setTypeName(PortType typeName) {
        this.typeName = typeName;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

}
