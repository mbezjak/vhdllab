package hr.fer.zemris.vhdllab.platform.support;

import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.ApplicationPageFactory;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.PageDescriptor;

public class SimplisticEclipseBasedApplicationPageFactory implements
        ApplicationPageFactory {

    @Override
    public ApplicationPage createApplicationPage(ApplicationWindow window,
            PageDescriptor descriptor) {
        SimplisticEclipseBasedApplicationPage page = new SimplisticEclipseBasedApplicationPage();
        page.setApplicationWindow(window);
        page.setDescriptor(descriptor);
        return page;
    }

}
