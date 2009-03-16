package hr.fer.zemris.vhdllab.service.ci;

import static org.apache.commons.lang.StringUtils.lowerCase;
import hr.fer.zemris.vhdllab.validation.ConsistentRangeConstraint;
import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

/**
 * Describes a <code>PORT</code> block of VHDL code.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
@ConsistentRangeConstraint
public class Port implements Serializable {

    private static final long serialVersionUID = -3505930700727412550L;

    @NotNull
    @Length(max = 255)
    @NameFormatConstraint
    private String name;
    @NotNull
    private PortDirection direction;
    @Min(value = 0)
    private Integer from;
    @Min(value = -1)
    private Integer to;

    public Port() {
    }

    public Port(Port clone) {
        setName(clone.name);
        setDirection(clone.direction);
        setFrom(clone.from);
        setTo(clone.to);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortDirection getDirection() {
        return direction;
    }

    public void setDirection(PortDirection direction) {
        this.direction = direction;
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

    public boolean isIN() {
        return PortDirection.IN.equals(direction);
    }

    public boolean isOUT() {
        return PortDirection.OUT.equals(direction);
    }

    public boolean isScalar() {
        return from == null && to == null;
    }

    public boolean isVector() {
        return from != null && to != null;
    }

    public boolean isDOWNTO() {
        if (!isVector()) {
            return false;
        }
        return from >= to;
    }

    public boolean isTO() {
        if (!isVector()) {
            return false;
        }
        return from < to;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(lowerCase(name))
                    .append(direction)
                    .append(from)
                    .append(to)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Port))
            return false;
        Port other = (Port) obj;
        return new EqualsBuilder()
                    .append(lowerCase(name), lowerCase(other.name))
                    .append(direction,other.direction)
                    .append(from, other.from)
                    .append(to, other.to)
                    .isEquals();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(name).append(": ");
        sb.append(direction).append(" ");
        if (isScalar()) {
            sb.append("STD_LOGIC");
        } else {
            sb.append("STD_LOGIC_VECTOR");
        }
        if (isVector()) {
            sb.append("(").append(from);
            if (isDOWNTO()) {
                sb.append(" DOWNTO ");
            } else {
                sb.append(" TO ");
            }
            sb.append(to).append(")");
        }
        return sb.toString();
    }

}
