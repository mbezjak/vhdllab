package hr.fer.zemris.vhdllab.platform.log;

public class StdOutConsumer extends AbstractStandardStreamConsumer {

    private static final StdOutConsumer INSTANCE = new StdOutConsumer();

    public static StdOutConsumer instance() {
        return INSTANCE;
    }

    @Override
    public void substituteStream() {
        System.setOut(createSubstituteStream(System.out));
    }

}
