/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.platform.gui.dialog.save;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.gui.dialog.AbstractDialog;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.Validate;

public class SaveDialog extends AbstractDialog<List<File>> {

    private static final long serialVersionUID = 1L;

    public static final String SHOULD_AUTO_SAVE = "should.auto.save";

    private static final String ALWAYS_SAVE_MESSAGE = "dialog.multi.save.always_save_resources";
    private static final String SELECT_ALL_MESSAGE = "dialog.multi.save.select_all";
    private static final String DESELECT_ALL_MESSAGE = "dialog.multi.save.deselect_all";
    private static final String OK_MESSAGE = "dialog.multi.save.ok";
    private static final String CANCEL_MESSAGE = "dialog.cancel";
    private static final String SAVE_RESOURCES_TITLE = "dialog.multi.save.resources.title";
    private static final String SAVE_RESOURCES_MESSAGE = "dialog.multi.save.resources.message";
    private static final String COMPILE_AFTER_SAVE_TITLE = "dialog.multi.save.compile_after_save.title";
    private static final String COMPILE_AFTER_SAVE_MESSAGE = "dialog.multi.save.compile_after_save.message";
    private static final String SIMULATE_AFTER_SAVE_TITLE = "dialog.multi.save.simulate_after_save.title";
    private static final String SIMULATE_AFTER_SAVE_MESSAGE = "dialog.multi.save.simulate_after_save.message";
    private static final String SHUTDOWN_AFTER_SAVE_TITLE = "dialog.multi.save.shutdown_after_save.title";
    private static final String SHUTDOWN_AFTER_SAVE_MESSAGE = "dialog.multi.save.shutdown_after_save.message";

    /** Size of a border */
    private static final int BORDER = 10;
    /** Width of this dialog */
    private static final int DIALOG_WIDTH = 350;
    /** Height of this dialog */
    private static final int DIALOG_HEIGHT = 450;
    /** Height of a label */
    private static final int LABEL_HEIGHT = 50;
    /** Width of all buttons */
    private static final int BUTTON_WIDTH = 100;
    /** Height of all buttons */
    private static final int BUTTON_HEIGHT = 24;

    CheckBoxList list;

    public SaveDialog(LocalizationSource source, SaveContext context) {
        super(source);
        Validate.notNull(context, "Save variant can't be null");
        // setup label
        JLabel label = new JLabel(getMainMessage(source, context));
        int width = DIALOG_WIDTH - 2 * BORDER;
        int height = LABEL_HEIGHT - 2 * BORDER;
        label.setPreferredSize(new Dimension(width, height));
        label.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER,
                BORDER));

        // setup check box list
        list = new CheckBoxList();
        width = DIALOG_WIDTH - 2 * BORDER;
        height = 0; // because list is a center component and it doesnt need
        // height
        list.setPreferredSize(new Dimension(width, height));
        list.setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER,
                BORDER));

        // setup select all and deselect all buttons
        JButton selectAll = new JButton(source.getMessage(SELECT_ALL_MESSAGE));
        selectAll.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                list.setSelectionToAll(true);
            }
        });

        JButton deselectAll = new JButton(source
                .getMessage(DESELECT_ALL_MESSAGE));
        deselectAll
                .setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        deselectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                list.setSelectionToAll(false);
            }
        });

        Box selectBox = Box.createHorizontalBox();
        selectBox.add(selectAll);
        selectBox
                .add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
        selectBox.add(deselectAll);
        selectBox.setBorder(BorderFactory.createEmptyBorder(0, 0, BORDER, 0));

        // setup ok and cancel buttons
        JButton ok = new JButton(source.getMessage(OK_MESSAGE));
        ok.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(new ArrayList<File>());
            }
        });

        JButton cancel = new JButton(source.getMessage(CANCEL_MESSAGE));
        cancel.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(null);
            }
        });

        Box actionBox = Box.createHorizontalBox();
        actionBox.add(ok);
        actionBox
                .add(Box.createRigidArea(new Dimension(BORDER, BUTTON_HEIGHT)));
        actionBox.add(cancel);
        actionBox.setBorder(BorderFactory.createEmptyBorder(BORDER, 0, BORDER,
                0));

        JPanel selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(selectBox, BorderLayout.EAST);
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.add(actionBox, BorderLayout.EAST);

        JCheckBox alwaysSave = new JCheckBox(source
                .getMessage(ALWAYS_SAVE_MESSAGE));
        alwaysSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                Preferences preferences = Preferences
                        .userNodeForPackage(SaveDialog.class);
                preferences.putBoolean(SHOULD_AUTO_SAVE, checkBox.isSelected());
            }
        });
        alwaysSave.setSelected(false);
        JPanel alwaysSavePanel = new JPanel(new BorderLayout());
        alwaysSavePanel.add(alwaysSave, BorderLayout.WEST);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(selectPanel, BorderLayout.NORTH);
        lowerPanel.add(alwaysSavePanel, BorderLayout.CENTER);
        lowerPanel.add(actionPanel, BorderLayout.SOUTH);

        this.setLayout(new BorderLayout());
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(label, BorderLayout.NORTH);
        messagePanel.add(list, BorderLayout.CENTER);
        messagePanel.add(lowerPanel, BorderLayout.SOUTH);
        this.getContentPane().add(messagePanel, BorderLayout.CENTER);
        this.getRootPane().setDefaultButton(ok);
        this.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        this.setTitle(getTitle(source, context));
    }

    private String getTitle(LocalizationSource source, SaveContext context) {
        switch (context) {
        case NORMAL:
            return source.getMessage(SAVE_RESOURCES_TITLE);
        case COMPILE_AFTER_SAVE:
            return source.getMessage(COMPILE_AFTER_SAVE_TITLE);
        case SIMULATE_AFTER_SAVE:
            return source.getMessage(SIMULATE_AFTER_SAVE_TITLE);
        case SHUTDOWN_AFTER_SAVE:
            return source.getMessage(SHUTDOWN_AFTER_SAVE_TITLE);
        default:
            throw new IllegalStateException("Unknown save context: " + context);
        }
    }

    private String getMainMessage(LocalizationSource source, SaveContext context) {
        switch (context) {
        case NORMAL:
            return source.getMessage(SAVE_RESOURCES_MESSAGE);
        case COMPILE_AFTER_SAVE:
            return source.getMessage(COMPILE_AFTER_SAVE_MESSAGE);
        case SIMULATE_AFTER_SAVE:
            return source.getMessage(SIMULATE_AFTER_SAVE_MESSAGE);
        case SHUTDOWN_AFTER_SAVE:
            return source.getMessage(SHUTDOWN_AFTER_SAVE_MESSAGE);
        default:
            throw new IllegalStateException("Unknown save context: " + context);
        }
    }

    @Override
    public void closeDialog(List<File> resourcesToSave) {
        if (resourcesToSave == null) {
            super.closeDialog(null);
        } else {
            for (SaveItem item : list.getItems()) {
                if (item.isSelected()) {
                    resourcesToSave.add(item.getFile());
                }
            }
            super.closeDialog(resourcesToSave);
        }
    }

    public void setSaveFiles(List<File> identifiers) {
        for (File i : identifiers) {
            list
                    .addItem(new SaveItem(true, i));
        }
    }

    /**
     * Represents an item that should be displayed in SaveDialog.
     * 
     * @author Miro Bezjak
     * @see SaveDialog
     */
    private static class SaveItem {

        private File file;
        /** Indicating if checkbox is selected */
        private boolean selected;

        public SaveItem(boolean selected, File file) {
            this.selected = selected;
            this.file = file;
        }

        /**
         * Getter for selected;
         * 
         * @return value of selected
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Setter for selected.
         * 
         * @param selected
         *            value to be set
         */
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public File getFile() {
            return file;
        }

        /**
         * Creates text out of project name and file name.
         * 
         * @return created text
         */
        public String getText() {
            return file.getName() + " [" + file.getProject().getName() + "]";
        }

    }

    /**
     * Class that displays a list of checkboxes.
     * 
     * @author Miro Bezjak
     * @see SaveDialog
     */
    private static class CheckBoxList extends JPanel {

        /**
         * Serial Version UID.
         */
        private static final long serialVersionUID = 4499815884621898659L;
        /** Items that are displayed to user. */
        private Map<JCheckBox, SaveItem> items;
        /** Container of checkboxes. */
        private Box box;

        /**
         * Constructor.
         */
        public CheckBoxList() {
            items = new LinkedHashMap<JCheckBox, SaveItem>();
            box = Box.createVerticalBox();
            JScrollPane scroll = new JScrollPane(box);
            scroll.getViewport().setBackground(Color.WHITE);
            this.setLayout(new BorderLayout());
            this.add(scroll, BorderLayout.CENTER);
        }

        /**
         * Add item to be displayed.
         * 
         * @param item
         *            item to display
         */
        public void addItem(SaveItem item) {
            JCheckBox checkBox = new JCheckBox(item.getText(), item
                    .isSelected());
            checkBox.setBackground(Color.WHITE);
            box.add(checkBox);
            items.put(checkBox, item);
        }

        /**
         * Returns <code>true</code> if there are no displayed items.
         * 
         * @return <code>true</code> if there are no displayed items;
         *         <code>false</code> otherwise.
         */
        public boolean isEmpty() {
            return items.isEmpty();
        }

        /**
         * Returns all displayed items.
         * 
         * @return all displayed items
         */
        public List<SaveItem> getItems() {
            List<SaveItem> allItems = new ArrayList<SaveItem>();
            for (Entry<JCheckBox, SaveItem> entry : items.entrySet()) {
                JCheckBox checkbox = entry.getKey();
                SaveItem item = entry.getValue();
                item.setSelected(checkbox.isSelected());
                allItems.add(item);
            }
            return allItems;
        }

        /**
         * Sets a selection to all checkboxes to a value of
         * <code>selected</code>.
         * 
         * @param selected
         *            <code>true</code> if checkboxes should be selected;
         *            <code>false</code> otherwise
         */
        public void setSelectionToAll(boolean selected) {
            for (JCheckBox c : items.keySet()) {
                c.setSelected(selected);
            }
        }

    }
}
