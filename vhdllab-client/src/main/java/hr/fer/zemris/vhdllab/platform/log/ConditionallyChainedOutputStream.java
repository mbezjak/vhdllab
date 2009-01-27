package hr.fer.zemris.vhdllab.platform.log;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

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
        if (shouldChainConditionally()) {
            printStream.write(b);
        }
        super.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        if (shouldChainConditionally()) {
            printStream.write(b, off, len);
        }
        super.write(b, off, len);
    }

    private boolean shouldChainConditionally() {
        return ApplicationContextHolder.getContext().isDevelopment();
    }

}
