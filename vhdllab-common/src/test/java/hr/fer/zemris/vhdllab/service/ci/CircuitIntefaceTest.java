package hr.fer.zemris.vhdllab.service.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Before;
import org.junit.Test;

public class CircuitIntefaceTest extends ValueObjectTestSupport {

    private CircuitInterface ci;

    @Before
    public void initEntity() {
        ci = new CircuitInterface();
    }

    @Test
    public void basics() {
        ci = new CircuitInterface();
        assertNull(ci.getName());
        assertNotNull(ci.getPorts());

        ci.setName("ci_name");
        assertEquals("ci_name", ci.getName());
        ci.setName(null);
        assertNull(ci.getName());
    }

    @Test
    public void constructorString() {
        ci = new CircuitInterface("ci_name");
        assertEquals("ci_name", ci.getName());
        assertNotNull(ci.getPorts());
    }

    @Test
    public void getPorts() {
        List<Port> ports = ci.getPorts();
        ports.add(new Port());
        assertEquals(1, ci.getPorts().size());
    }

    @Test
    public void addAll() {
        List<Port> ports = new ArrayList<Port>(2);
        ports.add(new Port());
        ports.add(new Port());
        ci.addAll(ports);
        assertEquals(2, ci.getPorts().size());
    }

    @Test
    public void add() {
        ci.add(new Port());
        assertEquals(1, ci.getPorts().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPortNullArgument() {
        ci.getPort(null);
    }

    @Test
    public void getPort() {
        String portName = "port_name";
        assertNull(ci.getPort(portName));

        Port port = new Port();
        port.setName(portName);
        ci.add(port);
        assertEquals(port, ci.getPort(portName));
        assertEquals(port, ci.getPort(portName.toUpperCase()));
    }

    @Test
    public void hashCodeAndEquals() throws Exception {
        ci.setName("ci_name");
        basicEqualsTest(ci);

        CircuitInterface another = (CircuitInterface) BeanUtils.cloneBean(ci);
        assertEqualsAndHashCode(ci, another);

        another.setName("another_name");
        assertNotEqualsAndHashCode(ci, another);

        another.setName("CI_name");
        assertEqualsAndHashCode(ci, another);

        another = (CircuitInterface) BeanUtils.cloneBean(ci);
        another.add(new Port());
        assertNotEqualsAndHashCode(ci, another);
    }

    @Test
    public void testToString() {
        ci.setName("ci_name");
        toStringPrint(ci);
        assertEquals("ENTITY ci_name IS PORT(\n);\nEND ci_name;", ci.toString());

        Port port = new Port();
        port.setName("a");
        port.setDirection(PortDirection.IN);
        ci.add(port);
        toStringPrint(ci);
        assertEquals(
                "ENTITY ci_name IS PORT(\n\ta: IN STD_LOGIC\n);\nEND ci_name;",
                ci.toString());

        port = new Port();
        port.setName("f");
        port.setDirection(PortDirection.OUT);
        ci.add(port);
        toStringPrint(ci);
        assertEquals(
                "ENTITY ci_name IS PORT(\n\ta: IN STD_LOGIC;\n\tf: OUT STD_LOGIC\n);\nEND ci_name;",
                ci.toString());
    }

}
