package hr.fer.zemris.vhdllab.platform.support;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationDescriptor;
import org.springframework.richclient.application.config.ApplicationLifecycleAdvisor;

public class VhdllabApplication extends Application {

    private boolean forceShutdownInProgress;

    public VhdllabApplication(ApplicationDescriptor descriptor,
            ApplicationLifecycleAdvisor advisor) {
        super(descriptor, advisor);
    }

    public boolean isForceShutdownInProgress() {
        return forceShutdownInProgress;
    }

    @Override
    public void close(boolean force, int exitCode) {
        forceShutdownInProgress = force;
        super.close(force, exitCode);
    }

}
