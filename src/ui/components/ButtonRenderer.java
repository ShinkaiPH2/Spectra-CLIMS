package ui.components;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Simple renderer that displays a consistent "Edit" button in a table cell.
 */
public class ButtonRenderer implements TableCellRenderer {
    private final JButton button;

    public ButtonRenderer() {
        button = new JButton("Edit");
        button.setFocusPainted(false);
        button.setBackground(new Color(144, 238, 144));
        button.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        return button;
    }
}
