package hr.fer.zemris.vhdllab.platform.gui;

import hr.fer.zemris.vhdllab.applets.main.component.statusbar.StatusBar;
import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.platform.gui.container.EditorTabbedPane;
import hr.fer.zemris.vhdllab.platform.gui.container.ViewTabbedPane;
import hr.fer.zemris.vhdllab.platform.gui.menu.MenuGenerator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import org.apache.commons.lang.Validate;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.springframework.context.ApplicationContext;

public final class VhdllabFrame extends JFrame implements
        PreferenceChangeListener {

    private static final long serialVersionUID = 1L;

    private ApplicationContext context;

    private JPanel projectExplorerPane;
    private JTabbedPane editorPane;
    private JTabbedPane viewPane;

    private JSplitPane horizontalSplitPane;
    private JSplitPane verticalSplitPane;

    private MaximizationManager maximizationManager;

    public void init() {
        initGUI();
        Preferences preferences = Preferences
                .userNodeForPackage(VhdllabFrame.class);
        preferences.addPreferenceChangeListener(this);

        Thread
                .setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        JOptionPane.showMessageDialog(VhdllabFrame.this,
                                "Error occurred: " + e.getMessage(),
                                "Error occurred", JOptionPane.ERROR_MESSAGE);
                    }
                });

        Logger.getRootLogger().addAppender(new AppenderSkeleton() {

            @Override
            public boolean requiresLayout() {
                return false;
            }

            @Override
            public void close() {
            }

            @Override
            protected void append(LoggingEvent event) {
                ThrowableInformation throwableInformation = event
                        .getThrowableInformation();
                if (throwableInformation != null) {
                    JOptionPane.showMessageDialog(VhdllabFrame.this,
                            "Error occurred: "
                                    + throwableInformation.getThrowable()
                                            .getMessage(), "Error occurred",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setPaneSize();
    }

    private JPanel setupStatusBar() {
        StatusBar statusBar = new StatusBar();
        JPanel statusBarPanel = new JPanel(new BorderLayout());
        statusBarPanel.add(statusBar, BorderLayout.CENTER);
        statusBarPanel.setPreferredSize(new Dimension(0, 24));
        return statusBarPanel;
    }

    private void initGUI() {
        MenuGenerator menuGenerator = (MenuGenerator) context
                .getBean("menuGenerator");
        maximizationManager = new MaximizationManager();

        projectExplorerPane = new JPanel(new BorderLayout());
        editorPane = new EditorTabbedPane(maximizationManager);
        editorPane.setComponentPopupMenu(menuGenerator
                .generateEditorPopupMenu());
        viewPane = new ViewTabbedPane(maximizationManager);

        horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                projectExplorerPane, editorPane);
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                horizontalSplitPane, viewPane);
        add(maximizationManager.getCenterPanel(verticalSplitPane),
                BorderLayout.CENTER);
        add(setupStatusBar(), BorderLayout.SOUTH);
        setJMenuBar(menuGenerator.generateMainMenu());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPaneSize();
            }
        });
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        String name = event.getKey();
        Preferences pref = event.getNode();
        if (name
                .equalsIgnoreCase(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH)) {
            double size = pref.getDouble(name, 0.15);
            horizontalSplitPane.setDividerLocation((int) (horizontalSplitPane
                    .getWidth() * size));
        } else if (name.equalsIgnoreCase(UserFileConstants.SYSTEM_VIEW_HEIGHT)) {
            double size = pref.getDouble(name, 0.75);
            verticalSplitPane.setDividerLocation((int) (verticalSplitPane
                    .getHeight() * size));
        } else if (name
                .equalsIgnoreCase(UserFileConstants.SYSTEM_TOOLTIP_DURATION)) {
            int duration = pref.getInt(
                    UserFileConstants.SYSTEM_TOOLTIP_DURATION, 15000);
            ToolTipManager.sharedInstance().setDismissDelay(duration);
        }
    }

    private void setPaneSize() {
        validate();
        double size;

        try {
            Preferences pref = Preferences
                    .userNodeForPackage(VhdllabFrame.class);
            size = pref.getDouble(
                    UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH, 0.15);
            horizontalSplitPane.setDividerLocation((int) (horizontalSplitPane
                    .getWidth() * size));

            size = pref.getDouble(UserFileConstants.SYSTEM_VIEW_HEIGHT, 0.75);
            verticalSplitPane.setDividerLocation((int) (verticalSplitPane
                    .getHeight() * size));
        } catch (RuntimeException e) {
            horizontalSplitPane.setDividerLocation((int) (horizontalSplitPane
                    .getWidth() * 0.15));
            verticalSplitPane.setDividerLocation((int) (verticalSplitPane
                    .getHeight() * 0.75));
        }
    }

    private void storePaneSize() {
        Preferences pref = Preferences.userNodeForPackage(VhdllabFrame.class);
        DecimalFormat formatter = new DecimalFormat("0.##");
        double size = horizontalSplitPane.getDividerLocation() * 1.0
                / horizontalSplitPane.getWidth();
        String property = formatter.format(size);
        pref.put(UserFileConstants.SYSTEM_PROJECT_EXPLORER_WIDTH, property);

        size = verticalSplitPane.getDividerLocation() * 1.0
                / verticalSplitPane.getHeight();
        property = formatter.format(size);
        pref.put(UserFileConstants.SYSTEM_VIEW_HEIGHT, property);
    }

    public void setProjectExplorer(JPanel component, String title) {
        Validate.notNull(component, "Project explorer component can't be null");
        Validate.notNull(title, "Project explorer's title can't be null");
        component.setBorder(BorderFactory.createTitledBorder(title));
        projectExplorerPane.add(component, BorderLayout.CENTER);
        projectExplorerPane.revalidate();
        projectExplorerPane.repaint();
    }

    public JTabbedPane getEditorPane() {
        return editorPane;
    }

    public JTabbedPane getViewPane() {
        return viewPane;
    }

    public VhdllabFrame(ApplicationContext context) {
        this.context = context;
        setTitle("VHDLLab");
        Dimension dimension = new Dimension(900, 700);
        setPreferredSize(dimension);
        setSize(dimension);

        URL resource = getClass().getClassLoader().getResource(
                "icons/vhdllab_main_16.png");
        setIconImage(new ImageIcon(resource).getImage());
        init();
        pack();
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

}
