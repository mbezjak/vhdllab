package hr.fer.zemris.vhdllab.service.ci;

import static org.apache.commons.lang.StringUtils.lowerCase;
import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Size;
import org.hibernate.validator.Valid;

/**
 * Describes an <code>ENTITY</code> block of VHDL code.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class CircuitInterface implements Serializable {

    private static final long serialVersionUID = -7941833394453145492L;

    @NotNull
    @Length(max = 255)
    @NameFormatConstraint
    private String name;
    @NotNull
    @Size(max = 30)
    @Valid
    private List<Port> ports;

    public CircuitInterface() {
        ports = new ArrayList<Port>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAll(List<Port> collection) {
        ports.addAll(collection);
    }

    public void add(Port port) {
        ports.add(port);
    }

    public Port getPort(String portName) {
        Validate.notNull(portName, "Port name can't be null");
        for (Port p : ports) {
            if (StringUtils.equalsIgnoreCase(p.getName(), portName)) {
                return p;
            }
        }
        return null;
    }

    public List<Port> getPorts() {
        return ports;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(lowerCase(name))
                    .append(ports)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CircuitInterface))
            return false;
        CircuitInterface other = (CircuitInterface) obj;
        return new EqualsBuilder()
                    .append(lowerCase(name), lowerCase(other.name))
                    .append(ports, other.ports)
                    .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(10 + ports.size() * 50);
        sb.append("ENTITY ").append(name).append(" IS PORT(\n");
        for (Port p : getPorts()) {
            sb.append("\t").append(p).append(";\n");
        }
        if (!ports.isEmpty()) {
            // remove last semi-colon
            sb.delete(sb.length() - 2, sb.length() - 1);
        }
        sb.append(");\nEND ").append(name).append(";");
        return sb.toString();
    }

}
