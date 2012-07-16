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
package hr.fer.zemris.vhdllab.applets.editor.automaton;

public class EntityParser {
    private String parsedEntity = null;
    private int inputWidth = 0;
    private int outputWidth = 0;

    public EntityParser(String data) {
        parsedEntity = data;
        unparseEntity();
    }

    private void unparseEntity() {
        String[] redovi = parsedEntity.split("\n");
        for (int i = 0; i < redovi.length; i++) {
            String[] pom = redovi[i].split(" ");
            int br = 1;
            if (pom[2].toUpperCase().equals("STD_LOGIC_VECTOR")) {
                br += Math.abs(Integer.parseInt(pom[3])
                        - Integer.parseInt(pom[4]));
            }
            if (pom[1].toUpperCase().equals("IN"))
                inputWidth += br;
            else
                outputWidth += br;
        }
    }

    public String getParsedEntity() {
        return parsedEntity;
    }

    public int getInputWidth() {
        return inputWidth;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

}
