package ui.views;

import dao.DeviceDAO;
import model.Device;
import model.User;
import ui.components.Sidebar;
import ui.components.StatusCellRenderer;
import ui.components.ButtonRenderer;
import ui.components.ButtonEditor;
import ui.dialogs.DeviceFormDialog;
import util.UIHelpers;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.regex.Pattern;

/**
 * UI for managing devices in the CLIMS system.
 * Allows users to view, add, edit, and search devices.
 */

public class ManageDevicesUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;
    private String[] statusFilters = null;
    private User currentUser;
    private JLabel timeLabel;
    private static final String[] TABLE_COLUMNS = { "id", "Device Number", "Computer Parts", "Status", "Location",
            "Functions" };

    /*
    *
    */
    public ManageDevicesUI(User currentUser) {
        this(currentUser, null);
    }

    /**
     * Constructor for ManageDevicesUI.
     *
     * @param currentUser   The currently logged-in user.
     * @param statusFilters Optional array of status filters to apply to the device list.
     */
    public ManageDevicesUI(User currentUser, String[] statusFilters) {
        this.currentUser = currentUser;
        this.statusFilters = statusFilters;
        setTitle("CLIMS - Manage Devices");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        init();
        UIHelpers.startClock(timeLabel);
    }

    /*
    * Initializes the UI components and layout.
    */
    private void init() {
        setLayout(new BorderLayout());

        // Left Sidebar - using the reusable component
        Sidebar sidebar = new Sidebar(currentUser, this, "MANAGE_DEVICES");
        add(sidebar, BorderLayout.WEST);

        // Main content area
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(Color.WHITE);

        // Top header
        timeLabel = new JLabel();
        JPanel header = UIHelpers.createHeader("MANAGE DEVICES", timeLabel, new Color(238, 232, 170));
        mainContent.add(header, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top action panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(Color.WHITE);

        JButton addButton = ui.UiUtils.makeButton("ADD DEVICES", new Color(238, 232, 170));
        addButton.setFont(new Font("Arial", Font.BOLD, 11));
        addButton.addActionListener(e -> {
            DeviceFormDialog dlg = new DeviceFormDialog(this, null, currentUser);
            dlg.setVisible(true);
            refresh();
        });
        actionPanel.add(addButton);

        JLabel searchLabel = new JLabel("SEARCH BAR");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 11));
        actionPanel.add(searchLabel);

        searchField = new JTextField(30);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        actionPanel.add(searchField);

        contentPanel.add(actionPanel, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(TABLE_COLUMNS, 0) {
            public boolean isCellEditable(int r, int c) {
                return c == 5; // Only "Functions" column editable
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        // Hide the ID column in the view but keep it in the model (used internally)
        // Hide the ID column but keep it in the model for internal use
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);

        // Use a dedicated StatusCellRenderer to keep coloring logic separate
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());

        // Use a button renderer/editor for the Functions column so UI action is
        // separated
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(this, currentUser, table, model));

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        mainContent.add(contentPanel, BorderLayout.CENTER);
        add(mainContent, BorderLayout.CENTER);

        refresh();

        // Double-click to edit
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int viewRow = table.getSelectedRow();
                    if (viewRow >= 0) {
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        int id = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
                        Device d = DeviceDAO.getById(id);
                        if (d != null) {
                            DeviceFormDialog dlg = new DeviceFormDialog(ManageDevicesUI.this, d, currentUser);
                            dlg.setVisible(true);
                            refresh();
                        }
                    }
                }
            }
        });

        // Search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void onChange() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    if (statusFilters == null)
                        sorter.setRowFilter(null);
                    else
                        sorter.setRowFilter(makeStatusRowFilter(statusFilters));
                } else {
                    try {
                        RowFilter<DefaultTableModel, Object> textFilter = RowFilter
                                .regexFilter("(?i)" + Pattern.quote(text));
                        if (statusFilters != null) {
                            RowFilter<DefaultTableModel, Object> statusFilter = makeStatusRowFilter(statusFilters);
                            sorter.setRowFilter(RowFilter.andFilter(java.util.Arrays.asList(statusFilter, textFilter)));
                        } else {
                            sorter.setRowFilter(textFilter);
                        }
                    } catch (Exception ex) {
                        sorter.setRowFilter(null);
                    }
                }
            }

            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            public void changedUpdate(DocumentEvent e) {
                onChange();
            }
        });
    }

    /*
    * Refreshes the device list in the table.
    */
    public void refresh() {
        model.setRowCount(0);
        List<Device> devices = DeviceDAO.getAll();
        for (Device d : devices) {
            model.addRow(new Object[] {
                    d.getId(),
                    d.getDeviceNumber(),
                    d.getType(),
                    d.getStatus(),
                    d.getLocation(),
                    "Edit"
            });
        }

        if (statusFilters != null && statusFilters.length > 0) {
            sorter.setRowFilter(makeStatusRowFilter(statusFilters));
        }
    }

    /*
    * Creates a RowFilter based on the provided status filters.
    */
    private RowFilter<DefaultTableModel, Object> makeStatusRowFilter(String[] statuses) {
        if (statuses == null || statuses.length == 0)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statuses.length; i++) {
            if (i > 0)
                sb.append("|");
            sb.append("(?:" + Pattern.quote(statuses[i]) + ")");
        }
        String regex = "(?i)^(?:" + sb.toString() + ")$";
        return RowFilter.regexFilter(regex, 3); // status column index
    }
}
