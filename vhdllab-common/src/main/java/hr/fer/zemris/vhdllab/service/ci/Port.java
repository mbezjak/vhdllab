/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.service.ci;

import static org.apache.commons.lang.StringUtils.lowerCase;
import hr.fer.zemris.vhdllab.validation.ConsistentRangeConstraint;
import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

/**
 * Describes a <code>PORT</code> block of VHDL code.
 */
@ConsistentRangeConstraint
public class Port implements Serializable {

    private static final long serialVersionUID = -3505930700727412550L;

    public static final String TYPE_STD_LOGIC = "STD_LOGIC";
    public static final String TYPE_STD_LOGIC_VECTOR = "STD_LOGIC_VECTOR";
    public static final String DIRECTION_DOWNTO = "DOWNTO";
    public static final String DIRECTION_TO = "TO";

    @NotEmpty
    @Length(max = 255)
    @NameFormatConstraint
    private String name;
    @NotNull
    private PortDirection direction;
    @Min(value = 0)
    private Integer from;
    @Min(value = 0)
    private Integer to;

    public Port() {
    }

    public Port(String name, PortDirection direction) {
        setName(name);
        setDirection(direction);
    }

    public Port(String name, PortDirection direction, int from, int to) {
        this(name, direction);
        setFrom(from);
        setTo(to);
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

    public String getTypeName() {
        return isScalar() ? TYPE_STD_LOGIC : TYPE_STD_LOGIC_VECTOR;
    }

    public String getDirectionName() {
        return isDOWNTO() ? DIRECTION_DOWNTO : DIRECTION_TO;
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
        sb.append(direction).append(" ").append(getTypeName());
        if (isVector()) {
            sb.append("(").append(from).append(" ");
            sb.append(getDirectionName()).append(" ");
            sb.append(to).append(")");
        }
        return sb.toString();
    }

}
