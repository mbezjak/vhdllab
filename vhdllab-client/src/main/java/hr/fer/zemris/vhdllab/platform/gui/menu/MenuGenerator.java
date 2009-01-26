package hr.fer.zemris.vhdllab.platform.gui.menu;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MenuGenerator extends LocalizationSupport {

    @Autowired
    private ApplicationContext context;

    public JMenuBar generateMainMenu() {
        JMenuBar menuBar = new JMenuBar();
        generateMenu("mainmenu.", menuBar);
        return menuBar;
    }

    public JPopupMenu generateEditorPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        generateMenu("editorpopupmenu.", popupMenu);
        return popupMenu;
    }

    public void generateMenu(String prefix, JComponent menuElement) {
        Map<String, JComponent> menus = new HashMap<String, JComponent>();
        menus.put("", menuElement);
        for (String key : getKeys()) {
            if (key.startsWith(prefix)) {
                key = key.trim();
                String originalKey = key;
                key = key.substring(prefix.length());
                if (key.startsWith("[dev]")) {
                    if (!ApplicationContextHolder.getContext().isDevelopment()) {
                        continue;
                    }
                    key = key.substring("[dev]".length());
                }
                String message = getMessage(originalKey, null);
                JMenuItem item = null;
                Action action = null;
                try {
                    action = (Action) context.getBean(getBeanName(key));
                    initialize(action, message);
                } catch (NoSuchBeanDefinitionException e) {
                    action = new EmptyAction();
                    initialize(action, message);
                    item = new JMenu(action);
                }
                int index = key.lastIndexOf('.');
                String parentMenuName;
                if (index == -1) {
                    parentMenuName = "";
                } else {
                    parentMenuName = key.substring(0, index);
                }

                if (item == null) {
                    item = new JMenuItem(action);
                }
                JComponent parentMenu = menus.get(parentMenuName);
                if (parentMenu instanceof JMenuItem
                        && !(parentMenu instanceof JMenu)) {
                    parentMenu = new JMenu(((JMenuItem) parentMenu).getAction());
                    menus.put(parentMenuName, parentMenu);
                }
                String menuName = key.substring(index + 1);
                if (menuName.equalsIgnoreCase("[separator]")) {
                    if(parentMenu instanceof JMenu) {
                        ((JMenu) parentMenu).addSeparator();
                    } else {
                        ((JPopupMenu) parentMenu).addSeparator();
                    }
                } else {
                    parentMenu.add(item);
                    menus.put(key, item);
                }
            }
        }
    }

    private List<String> getKeys() {
        List<String> keys = new LinkedList<String>();
        InputStream is = ClassLoader
                .getSystemResourceAsStream("menu.properties");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null)
                break;
            if (line.startsWith("#"))
                continue;
            keys.add(line.split("[=: ]")[0].trim());
        }

        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keys;
    }

    private String getBeanName(String key) {
        String[] keyParts = key.split("[\\.\\_]");
        for (int i = 0; i < keyParts.length; i++) {
            keyParts[i] = StringUtils.capitalize(keyParts[i]);
        }
        return StringUtils.uncapitalize(StringUtils.join(keyParts) + "Action");
    }

    private void initialize(Action action, String message) {
        processMessage(action, message.trim());
    }

    private void processMessage(Action action, String message) {
        String[] split = message.split("\\|");
        if (split.length == 2) {
            setShortDescription(action, split[1].trim());
        }
        split = split[0].split("@");
        setNameAndMnemonic(action, split[0].trim());
        if (split.length == 2) {
            setAccelerator(action, split[1].trim());
        }
    }

    private void setShortDescription(Action action, String description) {
        action.putValue(Action.SHORT_DESCRIPTION, description);
    }

    private void setNameAndMnemonic(Action action, String nameAndMnemonic) {
        String name = nameAndMnemonic;
        int index = name.indexOf('&');
        if (index > -1) {
            action.putValue(Action.MNEMONIC_KEY, name.codePointAt(index + 1));
            name = name.replace("&", "");
        }
        action.putValue(Action.NAME, name);
    }

    private void setAccelerator(Action action, String accelerator) {
        String[] split = accelerator.split("\\+");
        int mask = 0;
        int keyCode = 0;
        for (String s : split) {
            s = s.trim();
            if (s.equalsIgnoreCase("ctrl")) {
                mask += KeyEvent.CTRL_DOWN_MASK;
            } else if (s.equalsIgnoreCase("alt")) {
                mask += KeyEvent.ALT_DOWN_MASK;
            } else if (s.equalsIgnoreCase("shift")) {
                mask += KeyEvent.SHIFT_DOWN_MASK;
            } else if (s.matches("F[1-9]+")) {
                s = s.substring(1); // skip F letter
                if (s.length() <= 2) {
                    if (StringUtils.isNumeric(s)) {
                        keyCode = KeyEvent.VK_F1 - 1 + Integer.parseInt(s);
                    }
                }
            } else if (s.length() == 1) {
                if (StringUtils.isAlphanumeric(s)) {
                    keyCode = s.toUpperCase(Locale.ENGLISH).codePointAt(0);
                }
            }
        }
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, mask);
        action.putValue(Action.ACCELERATOR_KEY, keyStroke);
    }

}
