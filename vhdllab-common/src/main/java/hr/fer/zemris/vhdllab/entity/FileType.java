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
package hr.fer.zemris.vhdllab.entity;

/**
 * Defines all types that a file can have.
 */
public enum FileType {
    SOURCE, TESTBENCH, SCHEMA, AUTOMATON, SIMULATION, PREDEFINED;

    /**
     * Returns boolean indicating if type is a VHDL circuit.
     *
     * @return boolean indicating if type is a VHDL circuit
     */
    public boolean isCircuit() {
        switch (this) {
        case SOURCE:
        case SCHEMA:
        case AUTOMATON:
        case PREDEFINED:
            return true;
        default:
            return false;
        }
    }

    /**
     * Returns boolean indicating if type can be compiled.
     *
     * @return boolean indicating if type can be compiled
     */
    public boolean isCompilable() {
        return isCircuit() && !this.equals(PREDEFINED);
    }

    /**
     * Returns boolean indicating if type can be simulated.
     *
     * @return boolean indicating if type can be simulated
     */
    public boolean isSimulatable() {
        return this.equals(TESTBENCH);
    }

    public boolean canViewVhdl() {
        switch (this) {
        case SCHEMA:
        case AUTOMATON:
        case TESTBENCH:
            return true;
        default:
            return false;
        }
    }

}
