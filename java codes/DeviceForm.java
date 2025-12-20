
package com.IoT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DeviceForm extends JFrame implements ActionListener {
    JTextField deviceName, deviceType;
    JButton saveBtn, refreshBtn, deleteBtn;
    JTable deviceTable;
    DefaultTableModel tableModel;

    public DeviceForm() {
        setTitle("Device Management");
        setBounds(150, 150, 700, 500);
        setLayout(new BorderLayout());

        // Input Panel (Top)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setPreferredSize(new Dimension(700, 200));

        JLabel nameLbl = new JLabel("Device Name:");
        nameLbl.setBounds(50, 30, 100, 25);
        inputPanel.add(nameLbl);

        deviceName = new JTextField();
        deviceName.setBounds(160, 30, 200, 25);
        inputPanel.add(deviceName);

        JLabel typeLbl = new JLabel("Device Type:");
        typeLbl.setBounds(50, 70, 100, 25);
        inputPanel.add(typeLbl);

        deviceType = new JTextField();
        deviceType.setBounds(160, 70, 200, 25);
        inputPanel.add(deviceType);

        saveBtn = new JButton("Save");
        saveBtn.setBounds(160, 120, 90, 30);
        saveBtn.addActionListener(this);
        inputPanel.add(saveBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(260, 120, 90, 30);
        refreshBtn.addActionListener(this);
        inputPanel.add(refreshBtn);

        deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBounds(360, 120, 130, 30);
        deleteBtn.addActionListener(this);
        inputPanel.add(deleteBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Table Panel (Center)
        String[] columns = {"ID", "Device Name", "Device Type", "Created At"};
        tableModel = new DefaultTableModel(columns, 0);
        deviceTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deviceTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadDevices();

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadDevices() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM devices ORDER BY device_id ASC")) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("device_id"),
                    rs.getString("device_name"),
                    rs.getString("device_type"),
                    rs.getTimestamp("created_at")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading devices: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            saveDevice();
        } else if (e.getSource() == refreshBtn) {
            loadDevices();
        } else if (e.getSource() == deleteBtn) {
            deleteDevice();
        }
    }

    private void saveDevice() {
        String name = deviceName.getText().trim();
        String type = deviceType.getText().trim();
        
        if (name.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO devices(device_name, device_type) VALUES(?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Device saved successfully!");
            
            // Clear fields and reload table
            deviceName.setText("");
            deviceType.setText("");
            loadDevices();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteDevice() {
        int selectedRow = deviceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a device to delete!");
            return;
        }
        
        int deviceId = (int) tableModel.getValueAt(selectedRow, 0);
        String deviceName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete device: " + deviceName + "?",
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM devices WHERE device_id = ?")) {
                ps.setInt(1, deviceId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Device deleted successfully!");
                loadDevices();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting device: " + ex.getMessage());
            }
        }
    }
}