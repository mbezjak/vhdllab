package hr.fer.zemris.vhdllab.service.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class PortTest extends ValueObjectTestSupport {

    private Port port;

    @Before
    public void initObject() {
        port = new Port("port_name", PortDirection.IN, 4, 0);
    }

    @Test
    public void basics() {
        port = new Port();
        assertNull(port.getName());
        assertNull(port.getDirection());
        assertNull(port.getFrom());
        assertNull(port.getTo());

        port.setName("port_name");
        assertEquals("port_name", port.getName());
        port.setName(null);
        assertNull(port.getName());

        port.setDirection(PortDirection.IN);
        assertEquals(PortDirection.IN, port.getDirection());
        port.setDirection(null);
        assertNull(port.getDirection());

        port.setFrom(3);
        assertEquals(Integer.valueOf(3), port.getFrom());
        port.setFrom(null);
        assertNull(port.getFrom());

        port.setTo(5);
        assertEquals(Integer.valueOf(5), port.getTo());
        port.setTo(null);
        assertNull(port.getTo());
    }

    @Test
    public void constructorScalar() {
        Port another = new Port("port_name", PortDirection.IN);
        assertEquals("port_name", another.getName());
        assertEquals(PortDirection.IN, another.getDirection());
        assertNull(another.getFrom());
        assertNull(another.getTo());
    }

    @Test
    public void constructorVector() {
        Port another = new Port("port_name", PortDirection.IN, 2, 3);
        assertEquals("port_name", another.getName());
        assertEquals(PortDirection.IN, another.getDirection());
        assertEquals(Integer.valueOf(2), another.getFrom());
        assertEquals(Integer.valueOf(3), another.getTo());
    }

    @Test
    public void copyConstructor() {
        Port another = new Port(port);
        assertEquals(port.getName(), another.getName());
        assertEquals(port.getDirection(), another.getDirection());
        assertEquals(port.getFrom(), another.getFrom());
        assertEquals(port.getTo(), another.getTo());
    }

    @Test
    public void isINAndOUT() {
        port.setDirection(PortDirection.IN);
        assertTrue(port.isIN());
        assertFalse(port.isOUT());

        port.setDirection(PortDirection.OUT);
        assertFalse(port.isIN());
        assertTrue(port.isOUT());
    }

    @Test
    public void isScalarAndVector() {
        port.setFrom(null);
        port.setTo(null);
        assertTrue(port.isScalar());
        assertFalse(port.isVector());

        port.setFrom(3);
        assertFalse(port.isScalar());
        assertFalse(port.isVector());

        port.setFrom(null);
        port.setTo(5);
        assertFalse(port.isScalar());
        assertFalse(port.isVector());

        port.setFrom(7);
        assertFalse(port.isScalar());
        assertTrue(port.isVector());
    }

    @Test
    public void isDOWNTOAndTO() {
        port.setFrom(null);
        port.setTo(null);
        assertFalse(port.isDOWNTO());
        assertFalse(port.isTO());

        port.setFrom(5);
        assertFalse(port.isDOWNTO());
        assertFalse(port.isTO());

        port.setTo(4);
        assertTrue(port.isDOWNTO());
        assertFalse(port.isTO());

        port.setTo(7);
        assertFalse(port.isDOWNTO());
        assertTrue(port.isTO());
    }

    @Test
    public void hashCodeAndEquals() throws Exception {
        basicEqualsTest(port);

        Port another = new Port(port);
        assertEqualsAndHashCode(port, another);

        another.setName("another_name");
        assertNotEqualsAndHashCode(port, another);

        another.setName("PORT_name");
        assertEqualsAndHashCode(port, another);

        another = new Port(port);
        another.setDirection(PortDirection.OUT);
        assertNotEqualsAndHashCode(port, another);

        another = new Port(port);
        another.setFrom(13);
        assertNotEqualsAndHashCode(port, another);

        another = new Port(port);
        another.setTo(15);
        assertNotEqualsAndHashCode(port, another);
    }

    @Test
    public void testToString() {
        port.setName("port_name");
        port.setDirection(PortDirection.IN);
        port.setFrom(null);
        port.setTo(null);
        toStringPrint(port);
        assertEquals("port_name: IN STD_LOGIC", port.toString());

        port.setFrom(4);
        port.setTo(1);
        toStringPrint(port);
        assertEquals("port_name: IN STD_LOGIC_VECTOR(4 DOWNTO 1)", port
                .toString());

        port.setTo(null);
        toStringPrint(port);
        assertEquals("port_name: IN STD_LOGIC_VECTOR", port.toString());

        port.setTo(7);
        toStringPrint(port);
        assertEquals("port_name: IN STD_LOGIC_VECTOR(4 TO 7)", port.toString());
    }

}
