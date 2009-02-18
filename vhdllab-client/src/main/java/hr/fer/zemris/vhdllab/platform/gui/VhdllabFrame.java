package hr.fer.zemris.vhdllab.platform.gui;

import hr.fer.zemris.vhdllab.constants.UserFileConstants;
import hr.fer.zemris.vhdllab.platform.gui.container.EditorTabbedPane;
import hr.fer.zemris.vhdllab.platform.gui.container.ViewTabbedPane;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

public final class VhdllabFrame extends JFrame implements
        PreferenceChangeListener {

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

        setPaneSize();
    }

    private void initGUI() {
        maximizationManager = new MaximizationManager();

        projectExplorerPane = new JPanel(new BorderLayout());
        editorPane = new EditorTabbedPane(maximizationManager);
        viewPane = new ViewTabbedPane(maximizationManager);

        horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                projectExplorerPane, editorPane);
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                horizontalSplitPane, viewPane);
        add(maximizationManager.getCenterPanel(verticalSplitPane),
                BorderLayout.CENTER);
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

}
