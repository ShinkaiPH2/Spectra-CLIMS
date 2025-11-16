package ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import ui.UiUtils;

import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
            if (value != null) {
                Color backgroundColor = UiUtils.colorForStatus(value.toString());
                component.setBackground(backgroundColor);
            } else {
                component.setBackground(Color.WHITE);
            }
        }
        return component;
    }
}
