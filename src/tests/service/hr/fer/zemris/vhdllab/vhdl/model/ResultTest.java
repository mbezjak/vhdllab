package hr.fer.zemris.vhdllab.vhdl.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.vhdllab.utilities.SerializationUtil;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;
import hr.fer.zemris.vhdllab.vhdl.Message;
import hr.fer.zemris.vhdllab.vhdl.Result;
import hr.fer.zemris.vhdllab.vhdl.SimulationMessage;
import hr.fer.zemris.vhdllab.vhdl.SimulationResult;

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
	 * Test serializing object then deserializing and seeing that they are the same.
	 */
	@Test
	public void serialization4() {
		List<Message> messages = new ArrayList<Message>();
		messages.add(new Message("circuit_and", "error has occured"));
		Result result = new Result(Integer.valueOf(0), true, messages);
		
		Result result2 = (Result) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
	/**
	 * Test serializing object then deserializing and seeing that they are the same for compilation result.
	 */
	@Test
	public void serialization5() {
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
	public void serialization6() {
		List<SimulationMessage> messages = new ArrayList<SimulationMessage>();
		messages.add(new SimulationMessage("circuit_and", "error has occured"));
		SimulationResult result = new SimulationResult(Integer.valueOf(0), true, messages, "waveform");
		
		SimulationResult result2 = (SimulationResult) SerializationUtil.serializeThenDeserializeObject(result);
		
		assertEquals(result, result2);
		assertEquals(result.hashCode(), result2.hashCode());
	}
	
}