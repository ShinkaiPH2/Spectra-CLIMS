package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Small shared helpers for UI components to avoid duplicated code.
 */
public final class UiUtils {
    private UiUtils() {
    }

    /**
     * Create a small, green "Edit" JButton used in table cells.
     *
     * @return a configured JButton instance labeled "Edit"
     */
    public static JButton makeEditButton() {
        JButton b = new JButton("Edit");
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBackground(new Color(144, 238, 144));
        return b;
    }

    /**
     * Create a general-purpose button with the given text and background color.
     *
     * @param text button label text
     * @param bg   background color to apply
     * @return a configured JButton
     */
    public static JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        return b;
    }

    // Navigation-specific button with fixed size and style used in the sidebar
    /**
     * Create a navigation-styled button used in the sidebar. This button has
     * a fixed size and bold font.
     *
     * @param text button label text
     * @param bg   background color to use
     * @return a configured navigation JButton
     */
    public static JButton makeNavButton(String text, Color bg) {
        JButton b = makeButton(text, bg);
        b.setMaximumSize(new Dimension(260, 50));
        b.setPreferredSize(new Dimension(260, 50));
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setBorderPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    /**
     * Map a device status string to a background color used for status cells.
     * The mapping is case-insensitive and returns `Color.WHITE` for unknown
     * statuses or null input.
     *
     * @param status device status string (e.g. "New", "Broken")
     * @return Color used to render the status cell background
     */
    public static Color colorForStatus(String status) {
        if (status == null)
            return Color.WHITE;
        String s = status.toLowerCase();
        switch (s) {
            case "new":
                return new Color(198, 239, 206);
            case "old":
                return new Color(255, 249, 196);
            case "in use":
            case "in-use":
                return new Color(221, 235, 247);
            case "repaired":
                return new Color(230, 230, 250);
            case "under maintenance":
            case "under_maintenance":
                return new Color(255, 229, 153);
            case "disposed":
                return Color.LIGHT_GRAY;
            case "missing":
                return new Color(255, 215, 0);
            case "broken":
                return new Color(255, 102, 102);
            default:
                return Color.WHITE;
        }
    }
}
