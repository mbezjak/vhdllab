package hr.fer.zemris.vhdllab.platform.support;

import hr.fer.zemris.vhdllab.view.TabbedEditorsView;
import hr.fer.zemris.vhdllab.view.explorer.ProjectExplorerView;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.application.PageLayoutBuilder;
import org.springframework.richclient.application.support.AbstractApplicationPage;

public class SimplisticEclipseBasedApplicationPage extends
        AbstractApplicationPage implements PageLayoutBuilder {

    private final List<PageComponent> views = new ArrayList<PageComponent>();
    boolean addingView;

    private JPanel projectExplorerPane;
    private JPanel editorsPane;
    private JTabbedPane viewsTabbedPane;

    @Override
    protected JComponent createControl() {
        projectExplorerPane = new JPanel(new BorderLayout());
        editorsPane = new JPanel(new BorderLayout());
        viewsTabbedPane = new JTabbedPane(JTabbedPane.TOP,
                JTabbedPane.WRAP_TAB_LAYOUT);
        viewsTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // if we're adding a component, ignore change of active
                // component
                if (!addingView && getViewsTabbedPane().getSelectedIndex() >= 0) {
                    setActiveComponent(getComponent(getViewsTabbedPane()
                            .getSelectedIndex()));
                }
            }
        });

        final double horizontalLocation = 0.15;
        final double verticalLocation = 0.75;
        final JSplitPane horizontalPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, projectExplorerPane, editorsPane);
        horizontalPane.setDividerLocation(horizontalLocation);
        final JSplitPane verticalPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, horizontalPane, viewsTabbedPane);
        verticalPane.setDividerLocation(verticalLocation);

        JPanel maximizedPanel = new JPanel(new BorderLayout());
        maximizedPanel.add(verticalPane, BorderLayout.CENTER);
        JPanel control = new JPanel(new BorderLayout());
        control.add(maximizedPanel, BorderLayout.CENTER);
        control.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                horizontalPane.setDividerLocation(horizontalLocation);
                verticalPane.setDividerLocation(verticalLocation);
            }
        });

        this.getPageDescriptor().buildInitialLayout(this);

        if (viewsTabbedPane.getTabCount() > 0) {
            viewsTabbedPane.setSelectedIndex(0);
        }

        Logger.getRootLogger().addAppender(new AppenderSkeleton() {
            @Override
            public boolean requiresLayout() {
                return false;
            }

            @Override
            public void close() {
            }

            @SuppressWarnings("synthetic-access")
            @Override
            protected void append(LoggingEvent event) {
                if (event.getLevel().equals(Level.INFO)) {
                    Object message = event.getMessage();
                    if (message != null) {
                        getActiveWindow().getStatusBar().setMessage(
                                message.toString());
                    }
                }
            }
        });
        return control;
    }

    @Override
    public void addView(String viewDescriptorId) {
        showView(viewDescriptorId);
    }

    @Override
    protected void doAddPageComponent(PageComponent pageComponent) {
        if (pageComponent instanceof ProjectExplorerView) {
            projectExplorerPane.add(pageComponent.getContext().getPane()
                    .getControl(), BorderLayout.CENTER);
            projectExplorerPane.revalidate();
            projectExplorerPane.repaint();
        } else if (pageComponent instanceof TabbedEditorsView) {
            editorsPane.add(pageComponent.getContext().getPane().getControl(),
                    BorderLayout.CENTER);
            editorsPane.revalidate();
            editorsPane.repaint();
        } else {
            try {
                addingView = true;
                views.add(pageComponent);
                Icon viewIcon = getIconSource().getIcon("view.icon");
                viewsTabbedPane.addTab(pageComponent.getDisplayName(),
                        viewIcon, pageComponent.getContext().getPane()
                                .getControl(), pageComponent.getCaption());
            } finally {
                addingView = false;
            }
        }
    }

    @Override
    protected void doRemovePageComponent(PageComponent pageComponent) {
        if (pageComponent instanceof ProjectExplorerView) {
            projectExplorerPane.removeAll();
            projectExplorerPane.revalidate();
            projectExplorerPane.repaint();
        } else if (pageComponent instanceof TabbedEditorsView) {
            editorsPane.removeAll();
            editorsPane.revalidate();
            editorsPane.repaint();
        } else {
            viewsTabbedPane.removeTabAt(indexOf(pageComponent));
            views.remove(pageComponent);
        }
    }

    @Override
    protected boolean giveFocusTo(PageComponent pageComponent) {
        if (pageComponent instanceof ProjectExplorerView) {
            projectExplorerPane.requestFocusInWindow();
        } else if (pageComponent instanceof TabbedEditorsView) {
            editorsPane.requestFocusInWindow();
        } else {
            if (!views.contains(pageComponent)) {
                return false;
            }
            viewsTabbedPane.setSelectedIndex(indexOf(pageComponent));
        }
        return true;
    }

    private int indexOf(PageComponent component) {
        return views.indexOf(component);
    }

    PageComponent getComponent(int index) {
        return views.get(index);
    }

    public JTabbedPane getViewsTabbedPane() {
        return viewsTabbedPane;
    }

}
