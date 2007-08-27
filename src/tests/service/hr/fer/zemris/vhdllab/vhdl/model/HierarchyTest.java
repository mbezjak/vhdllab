package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.constants.FileTypes;
import hr.fer.zemris.vhdllab.utilities.SerializationUtil;

import org.junit.Test;

public class HierarchyTest {
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization() {
		Hierarchy h = new Hierarchy("new_project");
		Pair p1 = new Pair("new_file_1", FileTypes.FT_VHDL_SOURCE);
		Pair p2 = new Pair("new_file_2", FileTypes.FT_VHDL_STRUCT_SCHEMA);
		Pair p3 = new Pair("new_file_3", FileTypes.FT_VHDL_TB);
		p2.addParent("new_file_1");
		h.addPair(p1);
		h.addPair(p2);
		h.addPair(p3);
		
		Hierarchy h2 = (Hierarchy) SerializationUtil.serializeThenDeserializeObject(h);
		
		assertEquals(h, h2);
		assertEquals(h.hashCode(), h2.hashCode());
	}
	
}