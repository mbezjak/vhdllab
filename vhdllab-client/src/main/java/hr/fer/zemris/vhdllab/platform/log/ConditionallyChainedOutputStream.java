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

import hr.fer.zemris.vhdllab.platform.context.Environment;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.Validate;

public class ConditionallyChainedOutputStream extends ByteArrayOutputStream {

    private final PrintStream printStream;

    public ConditionallyChainedOutputStream(PrintStream printStream) {
        super();
        Validate.notNull(printStream, "Print stream can't be null");
        this.printStream = printStream;
    }

    @Override
    public synchronized void write(int b) {
        if (shouldChain()) {
            printStream.write(b);
        }
        super.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        if (shouldChain()) {
            printStream.write(b, off, len);
        }
        super.write(b, off, len);
    }

    private boolean shouldChain() {
        return Environment.isDevelopment();
    }

}
