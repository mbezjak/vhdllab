package hr.fer.zemris.vhdllab.platform.context;

public abstract class ApplicationContextHolder {

    private static final ApplicationContext context = new ApplicationContext();

    public static ApplicationContext getContext() {
        return context;
    }

}
