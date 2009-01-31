package hr.fer.zemris.vhdllab.platform.gui.container;

import hr.fer.zemris.vhdllab.platform.gui.MaximizationManager;

import java.awt.Component;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import org.apache.commons.lang.Validate;

public class ViewTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 1L;

    public ViewTabbedPane(MaximizationManager manager) {
        super(TOP, WRAP_TAB_LAYOUT);
        Validate.notNull(manager, "Maximization manager can't be null");
        addMouseListener(new MaximizeOnDoubleClickMouseListener(manager));
        addContainerListener(new MinimizeWhenNoMoreTabsContainerListener(
                manager));
    }

    @Override
    public void insertTab(String title, Icon icon, Component component,
            String tip, int index) {
        Icon tabIcon = icon;
        if (tabIcon == null) {
            String iconName = getIconName();
            URL url = this.getClass().getResource(iconName);
            tabIcon = new ImageIcon(url);
        }
        super.insertTab(title, tabIcon, component, tip, index);
    }

    protected String getIconName() {
        return "view.png";
    }

}
