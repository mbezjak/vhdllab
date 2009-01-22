package hr.fer.zemris.vhdllab.platform.manager.view;

public interface ViewMetadata {

    Class<? extends View> getViewClass();

    String getCode();

    boolean isCloseable();

}
