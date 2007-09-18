package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.utilities.SerializationUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.Message;
import hr.fer.zemris.vhdllab.vhdl.MessageType;
import hr.fer.zemris.vhdllab.vhdl.Result;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationMessage;
import hr.fer.zemris.vhdllab.vhdl.VHDLGenerationResult;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ResultTest {
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization() {
		Result result = new Result(Integer.valueOf(0), true, new ArrayList<Message>());
		
		Result result2 = (Result) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for compilation result.
	 */
	@Test
	public void serialization2() {
		CompilationResult result = new CompilationResult(Integer.valueOf(0), true, new ArrayList<CompilationMessage>());
		
		CompilationResult result2 = (CompilationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for simulation result.
	 */
	@Test
	public void serialization3() {
		SimulationResult result = new SimulationResult(Integer.valueOf(0), true, new ArrayList<SimulationMessage>(), "waveform");
		
		SimulationResult result2 = (SimulationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for vhdl generation result.
	 */
	@Test
	public void serialization4() {
		VHDLGenerationResult result = new VHDLGenerationResult(Integer.valueOf(0), true, new ArrayList<VHDLGenerationMessage>(), "vhdlcode");
		
		VHDLGenerationResult result2 = (VHDLGenerationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization5() {
		List<Message> messages = new ArrayList<Message>();
		messages.add(new Message("circuit_and", "error has occured", MessageType.ERROR));
		Result result = new Result(Integer.valueOf(0), true, messages);
		
		Result result2 = (Result) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for compilation result.
	 */
	@Test
	public void serialization6() {
		List<CompilationMessage> messages = new ArrayList<CompilationMessage>();
		messages.add(new CompilationMessage("circuit_and", "error has occured", 5, 20));
		CompilationResult result = new CompilationResult(Integer.valueOf(0), true, messages);
		
		CompilationResult result2 = (CompilationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for simulation result.
	 */
	@Test
	public void serialization7() {
		List<SimulationMessage> messages = new ArrayList<SimulationMessage>();
		messages.add(new SimulationMessage("circuit_and", "error has occured"));
		SimulationResult result = new SimulationResult(Integer.valueOf(0), true, messages, "waveform");
		
		SimulationResult result2 = (SimulationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for vhdl generation result.
	 */
	@Test
	public void serialization8() {
		List<VHDLGenerationMessage> messages = new ArrayList<VHDLGenerationMessage>();
		messages.add(new VHDLGenerationMessage("circuit_and", "error has occured"));
		VHDLGenerationResult result = new VHDLGenerationResult(Integer.valueOf(0), true, messages, "vhdlcode");
		
		VHDLGenerationResult result2 = (VHDLGenerationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
}