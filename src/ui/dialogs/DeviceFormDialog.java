package ui.dialogs;

import dao.DeviceDAO;
import dao.LogDAO;
import model.Device;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeviceFormDialog extends JDialog {
    private JTextField deviceNumberField;
    private JComboBox<String> typeCombo;
    private JTextField brandField;
    private JTextField modelField;
    private JComboBox<String> statusCombo;
    private JComboBox<String> locationCombo;
    private JTextField purchaseDateField;
    private JTextField costField;

    private Device editing;
    private User currentUser;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    private JTextArea notesArea;

    public DeviceFormDialog(Frame owner, Device d, User currentUser) {
        super(owner, true);
        this.editing = d;
        this.currentUser = currentUser;
        setTitle(d == null ? "Add Device" : "Edit Device");
        setSize(420, 520);
        setLocationRelativeTo(owner);
        init();
    }

    private void init() {
        JPanel p = new JPanel(null);

        int y = 10;
        deviceNumberField = addField(p, "Device Number:", 10, y);
        y += 40;

        // type combo
        String[] types = new String[] { "Desktop", "Laptop", "Monitor", "Printer", "Accessory", "Other" };
        typeCombo = addCombo(p, "Type:", types, 10, y);
        y += 40;

        brandField = addField(p, "Brand:", 10, y);
        y += 40;
        modelField = addField(p, "Model:", 10, y);
        y += 40;

        // status combo (per spec)
        String[] statuses = new String[] { "New", "Old", "In Use", "Repaired", "Under Maintenance", "Disposed",
                "Missing", "Broken" };
        statusCombo = addCombo(p, "Status:", statuses, 10, y);
        y += 40;

        // location combo (sample locations)
        String[] locations = new String[] { "Lab A", "Lab B", "Storage", "Office", "Other" };
        locationCombo = addCombo(p, "Location:", locations, 10, y);
        y += 40;

        // notes area
        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(10, y, 120, 25);
        p.add(notesLabel);
        notesArea = new JTextArea();
        JScrollPane notesSp = new JScrollPane(notesArea);
        notesSp.setBounds(140, y, 220, 80);
        p.add(notesSp);
        y += 90;

        purchaseDateField = addField(p, "Purchase Date:", 10, y);
        y += 40;
        costField = addField(p, "Cost:", 10, y);
        y += 40;

        JButton save = new JButton(editing == null ? "Save" : "Update");
        // position buttons differently when editing so we can show Delete
        if (editing == null) {
            save.setBounds(60, y, 100, 30);
        } else {
            save.setBounds(40, y, 100, 30);
        }
        save.addActionListener(e -> onSave());
        p.add(save);

        if (editing != null) {
            JButton delete = new JButton("Delete");
            delete.setBounds(160, y, 100, 30);
            delete.setBackground(Color.RED);
            delete.setForeground(Color.WHITE);
            delete.addActionListener(e -> onDelete());
            p.add(delete);

            JButton cancel = new JButton("Cancel");
            cancel.setBounds(280, y, 100, 30);
            cancel.addActionListener(e -> dispose());
            p.add(cancel);
        } else {
            JButton cancel = new JButton("Cancel");
            cancel.setBounds(200, y, 100, 30);
            cancel.addActionListener(e -> dispose());
            p.add(cancel);
        }

        getContentPane().add(p);

        // If editing an existing device, populate fields
        if (editing != null) {
            deviceNumberField.setText(editing.getDeviceNumber());
            typeCombo.setSelectedItem(editing.getType());
            brandField.setText(editing.getBrand());
            modelField.setText(editing.getModel());
            statusCombo.setSelectedItem(editing.getStatus());
            locationCombo.setSelectedItem(editing.getLocation());
            notesArea.setText(editing.getNotes());
            purchaseDateField.setText(editing.getPurchaseDate());
            costField.setText(String.valueOf(editing.getCost()));
        }
    }

    private JTextField addField(JPanel p, String label, int x, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 120, 25);
        p.add(l);
        JTextField f = new JTextField();
        f.setBounds(x + 130, y, 220, 25);
        p.add(f);
        return f;
    }

    private JComboBox<String> addCombo(JPanel p, String label, String[] items, int x, int y) {
        JLabel l = new JLabel(label);
        l.setBounds(x, y, 120, 25);
        p.add(l);
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setEditable(false);
        cb.setBounds(x + 130, y, 220, 25);
        p.add(cb);
        return cb;
    }

    private void onSave() {
        // basic validation
        String devNum = deviceNumberField.getText().trim();
        if (devNum.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Device Number is required", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (editing == null) {
            Device d = new Device();
            d.setDeviceNumber(devNum);
            d.setType((String) typeCombo.getSelectedItem());
            d.setBrand(brandField.getText().trim());
            d.setModel(modelField.getText().trim());
            d.setStatus((String) statusCombo.getSelectedItem());
            d.setLocation((String) locationCombo.getSelectedItem());
            d.setNotes(notesArea.getText().trim());
            d.setPurchaseDate(purchaseDateField.getText().trim());
            try {
                d.setCost(Double.parseDouble(costField.getText().trim()));
            } catch (Exception ex) {
                d.setCost(0);
            }

            boolean ok = DeviceDAO.insert(d);
            if (ok) {
                // log action
                try {
                    if (currentUser != null)
                        LogDAO.insertActionLog(currentUser.getId(), "Added device: " + d.getDeviceNumber(),
                                LocalDateTime.now().format(dtf));
                } catch (Exception ex) {
                }
                JOptionPane.showMessageDialog(this, "Saved");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Save failed");
            }
        } else {
            editing.setDeviceNumber(devNum);
            editing.setType((String) typeCombo.getSelectedItem());
            editing.setBrand(brandField.getText().trim());
            editing.setModel(modelField.getText().trim());
            editing.setStatus((String) statusCombo.getSelectedItem());
            editing.setLocation((String) locationCombo.getSelectedItem());
            editing.setNotes(notesArea.getText().trim());
            editing.setPurchaseDate(purchaseDateField.getText().trim());
            try {
                editing.setCost(Double.parseDouble(costField.getText().trim()));
            } catch (Exception ex) {
                editing.setCost(0);
            }

            boolean ok = DeviceDAO.update(editing);
            if (ok) {
                try {
                    if (currentUser != null)
                        LogDAO.insertActionLog(currentUser.getId(),
                                "Updated device: " + editing.getDeviceNumber() + " (ID " + editing.getId() + ")",
                                LocalDateTime.now().format(dtf));
                } catch (Exception ex) {
                }
                JOptionPane.showMessageDialog(this, "Updated");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed");
            }
        }
    }

    private void onDelete() {
        if (editing == null)
            return;
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete device: " + editing.getDeviceNumber() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION)
            return;

        boolean ok = DeviceDAO.delete(editing.getId());
        if (ok) {
            try {
                if (currentUser != null)
                    LogDAO.insertActionLog(currentUser.getId(),
                            "Deleted device: " + editing.getDeviceNumber() + " (ID " + editing.getId() + ")",
                            LocalDateTime.now().format(dtf));
            } catch (Exception ex) {
            }
            JOptionPane.showMessageDialog(this, "Deleted");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Delete failed");
        }
    }
}
