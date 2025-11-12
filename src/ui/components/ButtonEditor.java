package ui.components;

import dao.DeviceDAO;
import model.Device;
import model.User;
import ui.dialogs.DeviceFormDialog;
import ui.views.ManageDevicesUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cell editor that shows an Edit button and opens the DeviceFormDialog when
 * clicked.
 * Keeps the edit action logic separate from the main UI class.
 */
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private final ManageDevicesUI parent;
    private final User currentUser;
    private final JTable table;
    private final DefaultTableModel model;

    public ButtonEditor(ManageDevicesUI parent, User currentUser, JTable table, DefaultTableModel model) {
        super(new JCheckBox()); // DefaultCellEditor requires a component
        this.parent = parent;
        this.currentUser = currentUser;
        this.table = table;
        this.model = model;
        button = new JButton("Edit");
        button.setOpaque(true);
        button.setBackground(new Color(144, 238, 144));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // stop editing to trigger getCellEditorValue
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            try {
                Object idObj = model.getValueAt(modelRow, 0);
                int id = Integer.parseInt(idObj.toString());
                Device d = DeviceDAO.getById(id);
                if (d != null) {
                    DeviceFormDialog dlg = new DeviceFormDialog(parent, d, currentUser);
                    dlg.setVisible(true);
                }
            } catch (Exception ex) {
                // ignore minor errors here to avoid blocking UI
            } finally {
                // Refresh after editingStopped completes to avoid row index mismatches
                SwingUtilities.invokeLater(() -> parent.refresh());
            }
        }
        return "Edit";
    }
}
