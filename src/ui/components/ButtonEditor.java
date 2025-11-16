package ui.components;

import dao.DeviceDAO;
import model.Device;
import model.User;
import ui.UiUtils;
import ui.dialogs.DeviceFormDialog;
import ui.views.ManageDevicesUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Cell editor that shows an Edit button and opens the DeviceFormDialog when
 * clicked.
 * Keeps the edit action logic separate from the main UI class.
 */
public class ButtonEditor extends DefaultCellEditor {
    private final JButton editButton;
    private final ManageDevicesUI parentUI;
    private final User currentUser;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ButtonEditor(ManageDevicesUI parentUI, User currentUser, JTable table, DefaultTableModel tableModel) {
        super(new JCheckBox()); // DefaultCellEditor requires a component
        this.parentUI = parentUI;
        this.currentUser = currentUser;
        this.table = table;
        this.tableModel = tableModel;
        this.editButton = UiUtils.makeEditButton();
        // Stop editing when the button is clicked; this will call getCellEditorValue
        this.editButton.addActionListener(e -> fireEditingStopped());
    }

    /**
     * Create a ButtonEditor.
     *
     * @param parentUI    the parent `ManageDevicesUI` used to refresh the table
     *                    after editing
     * @param currentUser the currently logged-in user (passed to the edit dialog)
     * @param table       the `JTable` this editor belongs to
     * @param tableModel  the table model used to read the row id from column 0
     */

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return editButton;
    }

    /**
     * Return the editor component (an Edit button) for the table cell.
     * The actual edit action is triggered when editing stops and
     * `getCellEditorValue()` is called.
     *
     * @param table      the table being edited
     * @param value      the cell value (unused)
     * @param isSelected whether the cell is selected
     * @param row        the row index of the cell in view coordinates
     * @param column     the column index of the cell in view coordinates
     * @return the Edit button component
     */

    @Override
    public Object getCellEditorValue() {
        int viewRow = table.getSelectedRow();
        if (viewRow >= 0) {
            int modelRow = table.convertRowIndexToModel(viewRow);
            try {
                Object idObject = tableModel.getValueAt(modelRow, 0);
                int id = parseIdSafe(idObject);
                if (id != -1) {
                    Device device = DeviceDAO.getById(id);
                    if (device != null) {
                        DeviceFormDialog dialog = new DeviceFormDialog(parentUI, device, currentUser);
                        dialog.setVisible(true);
                    }
                }
            } catch (Exception ignored) {
                // Keep UI responsive: ignore non-critical exceptions here
            } finally {
                // Refresh after editingStopped completes to avoid row index mismatches
                SwingUtilities.invokeLater(() -> parentUI.refresh());
            }
        }
        return "Edit";
    }

    /**
     * Called when editing stops. This method looks up the device id from the
     * table model, opens the edit dialog for that device, and schedules a
     * refresh of the parent UI once the dialog closes.
     *
     * @return a placeholder value used by the table editor ("Edit")
     */

    private int parseIdSafe(Object idObject) {
        if (idObject == null) {
            return -1;
        }
        try {
            return Integer.parseInt(idObject.toString());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Safely parse an object into an int id. Returns -1 for null or
     * non-numeric values to indicate an invalid id.
     *
     * @param idObject the object to parse (often Integer or String)
     * @return parsed int id or -1 if parsing failed
     */
}
