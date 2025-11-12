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
    private static final DateTimeFormatter DEFAULT_DTF = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

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

    public static void startClock(JLabel timeLabel) {
        javax.swing.Timer t = new javax.swing.Timer(1000,
                e -> timeLabel.setText(LocalDateTime.now().format(DEFAULT_DTF)));
        t.setInitialDelay(0);
        t.start();

        // Try to stop the timer when the top-level window is closed so the EDT
        // doesn't keep running due to active timers.
        Window w = SwingUtilities.getWindowAncestor(timeLabel);
        if (w != null) {
            w.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    t.stop();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    t.stop();
                }
            });
        } else {
            // If the component is not yet displayable, attach a HierarchyListener
            // to wait until it's added to a window and then attach the listener.
            timeLabel.addHierarchyListener(new HierarchyListener() {
                @Override
                public void hierarchyChanged(HierarchyEvent e) {
                    if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0
                            && timeLabel.isDisplayable()) {
                        Window win = SwingUtilities.getWindowAncestor(timeLabel);
                        if (win != null) {
                            win.addWindowListener(new WindowAdapter() {
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
                        // Remove this listener after attaching
                        timeLabel.removeHierarchyListener(this);
                    }
                }
            });
        }
    }
}
