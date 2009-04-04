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
