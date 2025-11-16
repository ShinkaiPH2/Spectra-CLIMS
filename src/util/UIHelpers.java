package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * Small UI helper utilities shared between multiple screens.
 */
public class UIHelpers {
    /**
     * Default date/time formatter used across the UI for display (example: 11/16/2025 03:45:12 PM).
     */
    public static final DateTimeFormatter DEFAULT_DTF = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

    /**
     * Create a simple header panel with a title on the left and a time label on the
     * right.
     *
     * @param title     the header title text
     * @param timeLabel a JLabel that will display the current time
     * @param bgColor   background color of the header
     * @return a configured header JPanel
     */
    public static JPanel createHeader(String title, JLabel timeLabel, Color bgColor) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(titleLabel, BorderLayout.WEST);

        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        header.add(timeLabel, BorderLayout.EAST);

        return header;
    }

    /**
     * Create an informational card panel used on the dashboard.
     * The card contains a large count label (center) and a title (bottom).
     *
     * @param title   card title text
     * @param bgColor background color for the card
     * @return a configured card JPanel
     */
    public static JPanel createCard(String title, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel countLabel = new JLabel("0", SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 48));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        card.add(countLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        card.add(titleLabel, BorderLayout.SOUTH);

        // JLabel viewDetails = new JLabel("VIEW DETAILS >", SwingConstants.RIGHT);
        // viewDetails.setFont(new Font("Arial", Font.BOLD, 11));
        // viewDetails.setForeground(Color.WHITE);
        // viewDetails.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // card.add(viewDetails, BorderLayout.NORTH);

        return card;
    }

    /**
     * Start a one-second Swing Timer that updates the provided `timeLabel` with
     * the current timestamp. The timer is stopped automatically when the
     * top-level window containing the label is closed.
     *
     * @param timeLabel JLabel to update with the current date/time
     */
    public static void startClock(JLabel timeLabel) {
        javax.swing.Timer t = new javax.swing.Timer(1000,
                e -> timeLabel.setText(LocalDateTime.now().format(DEFAULT_DTF)));
        t.setInitialDelay(0);
        t.start();

        // Try to stop the timer when the top-level window is closed so the EDT
        // doesn't keep running due to active timers.
        // Attach a listener on the top-level window to stop the timer when the
        // window is closing/closed. If the label is not yet displayable, wait
        // until it is and attach the listener then.
        Runnable attachStopper = () -> {
            Window window = SwingUtilities.getWindowAncestor(timeLabel);
            if (window != null) {
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        t.stop();
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        t.stop();
                    }
                });
            }
        };

        if (timeLabel.isDisplayable()) {
            attachStopper.run();
        } else {
            timeLabel.addHierarchyListener(new HierarchyListener() {
                @Override
                public void hierarchyChanged(HierarchyEvent e) {
                    if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0
                            && timeLabel.isDisplayable()) {
                        attachStopper.run();
                        timeLabel.removeHierarchyListener(this);
                    }
                }
            });
        }
    }
}
