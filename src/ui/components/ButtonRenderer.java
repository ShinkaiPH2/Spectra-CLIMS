package ui.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import ui.UiUtils;

import java.awt.*;

/**
 * Simple renderer that displays a consistent "Edit" button in a table cell.
 */
public class ButtonRenderer implements TableCellRenderer {
    private final JButton editButton;

    public ButtonRenderer() {
        this.editButton = UiUtils.makeEditButton();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        return editButton;
    }
}
