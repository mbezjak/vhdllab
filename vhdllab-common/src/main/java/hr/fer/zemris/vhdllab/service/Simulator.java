package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.exception.CompilationException;
import hr.fer.zemris.vhdllab.service.exception.SimulationException;

public interface Simulator {

    CompilationResult compile(File file) throws CompilationException;

    SimulationResult simulate(File file) throws SimulationException;

}
