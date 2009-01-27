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
