package hr.fer.zemris.vhdllab.platform;

import hr.fer.zemris.vhdllab.platform.support.CommandLineArgumentProcessor;
import hr.fer.zemris.vhdllab.platform.support.GuiInitializer;
import hr.fer.zemris.vhdllab.platform.workspace.support.WorkspaceInitializer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Main {

    public static void main(String[] args) {
        processCommandLine(args);
        ApplicationContext context = setupDependencyInjectionContainer();
        setupGUI(context);
        initializeWorkspace(context);
    }

    private static void processCommandLine(String[] args) {
        CommandLineArgumentProcessor processor = new CommandLineArgumentProcessor();
        processor.processCommandLine(args);
    }

    private static ApplicationContext setupDependencyInjectionContainer() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                "applicationContext.xml");
        context.registerShutdownHook();
        return context;
    }

    private static void setupGUI(ApplicationContext context) {
        GuiInitializer initializer = new GuiInitializer(context);
        initializer.initGUI();
    }

    private static void initializeWorkspace(ApplicationContext context) {
        WorkspaceInitializer initializer = (WorkspaceInitializer) context
                .getBean("workspaceInitializer");
        initializer.initializeWorkspace();
    }

}
