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
package hr.fer.zemris.vhdllab.platform.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class AbstractStandardStreamConsumer {

    private ByteArrayOutputStream bos;

    protected PrintStream createSubstituteStream(PrintStream originalPrintSteam) {
        bos = new ConditionallyChainedOutputStream(originalPrintSteam);
        return new PrintStream(bos, true);
    }

    public abstract void substituteStream();

    @Override
    public String toString() {
        return bos.toString();
    }

}
