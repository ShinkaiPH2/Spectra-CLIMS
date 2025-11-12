package ui.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            String s = value.toString().toLowerCase();
            Color bg = Color.WHITE;
            switch (s) {
                case "new":
                    bg = new Color(198, 239, 206); // light green
                    break;
                case "old":
                    bg = new Color(255, 249, 196); // light yellow/beige
                    break;
                case "in use":
                case "in-use":
                    bg = new Color(221, 235, 247); // light blue
                    break;
                case "repaired":
                    bg = new Color(230, 230, 250); // lavender/purple-ish
                    break;
                case "under maintenance":
                case "under_maintenance":
                    bg = new Color(255, 229, 153); // orange-ish
                    break;
                case "disposed":
                    bg = Color.LIGHT_GRAY;
                    break;
                case "missing":
                    bg = new Color(255, 215, 0); // gold
                    break;
                case "broken":
                    bg = new Color(255, 102, 102); // red-ish
                    break;
                default:
                    bg = Color.WHITE;
            }
            if (!isSelected) {
                c.setBackground(bg);
            }
        } else {
            if (!isSelected)
                c.setBackground(Color.WHITE);
        }
        return c;
    }
}
