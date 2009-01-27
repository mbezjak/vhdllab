package hr.fer.zemris.vhdllab.platform.log;

public class StdErrConsumer extends AbstractStandardStreamConsumer {

    private static final StdErrConsumer INSTANCE = new StdErrConsumer();

    public static StdErrConsumer instance() {
        return INSTANCE;
    }

    @Override
    public void substituteStream() {
        System.setErr(createSubstituteStream(System.err));
    }

}
