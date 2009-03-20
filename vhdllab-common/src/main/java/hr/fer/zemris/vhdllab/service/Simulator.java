package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.service.exception.CompilationException;
import hr.fer.zemris.vhdllab.service.exception.SimulationException;
import hr.fer.zemris.vhdllab.service.exception.SimulatorTimeoutException;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.List;

public interface Simulator {

    List<CompilationMessage> compile(Integer fileId)
            throws CompilationException, SimulatorTimeoutException;

    Result simulate(Integer fileId) throws SimulationException,
            SimulatorTimeoutException;

}
