package hr.fer.zemris.vhdllab.api.vhdl;

import hr.fer.zemris.vhdllab.entity.validation.NameFormatConstraintValidator;

import java.io.Serializable;

/**
 * Describes a port in <code>ENTITY</code> block of VHDL code.
 * <p>
 * This class is immutable and therefor thread-safe.
 * </p>
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 * @see PortDirection
 * @see Type
 */
public final class Port implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * A port name.
     */
    private final String name;

    /**
     * A port direction.
     */
    private final PortDirection direction;

    /**
     * A port type.
     */
    private final Type type;

    /**
     * Constructs a port out of specified parameters.
     *
     * @param name
     *            a name of a port
     * @param direction
     *            a port direction
     * @param type
     *            a type of a port
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if name is not correct port name
     */
    public Port(String name, PortDirection direction, Type type) {
        if (name == null) {
            throw new NullPointerException("Port name cant be null");
        }
        if (direction == null) {
            throw new NullPointerException("Port direction cant be null");
        }
        if (type == null) {
            throw new NullPointerException("Port type cant be null");
        }
        this.name = name;
        if (!new NameFormatConstraintValidator().isValid(this)) {
            throw new IllegalArgumentException("Name is not correct port name");
        }
        this.direction = direction;
        this.type = type;
    }

    /**
     * Returns a name of a port.
     *
     * @return a name of a port
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a port direction.
     *
     * @return a port direction
     */
    public PortDirection getDirection() {
        return direction;
    }

    /**
     * Returns a type of a port.
     *
     * @return a type of a port
     */
    public Type getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.toLowerCase().hashCode();
        result = prime * result + direction.hashCode();
        result = prime * result + type.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Port))
            return false;
        final Port other = (Port) obj;
        if (!name.equalsIgnoreCase(other.name))
            return false;
        if (!direction.equals(other.direction))
            return false;
        if (!type.equals(other.type))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(40);
        sb.append(name).append(": ");
        sb.append(direction).append(" ");
        sb.append(type);
        return sb.toString();
    }

    /**
     * Make a defensive copy.
     */
    private Object readResolve() {
        return new Port(name, direction, type);
    }

}
