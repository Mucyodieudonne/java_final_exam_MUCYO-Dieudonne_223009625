
package com.IoT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SensorForm extends JFrame implements ActionListener {
    JTextField sensorName, sensorType;
    JButton saveBtn, refreshBtn, deleteBtn;
    JTable sensorTable;
    DefaultTableModel tableModel;

    public SensorForm() {
        setTitle("Sensor Management");
        setBounds(150, 150, 700, 500);
        setLayout(new BorderLayout());

        // Input Panel (Top)
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(null);
        inputPanel.setPreferredSize(new Dimension(700, 200));

        JLabel nameLbl = new JLabel("Sensor Name:");
        nameLbl.setBounds(50, 30, 100, 25);
        inputPanel.add(nameLbl);

        sensorName = new JTextField();
        sensorName.setBounds(160, 30, 200, 25);
        inputPanel.add(sensorName);

        JLabel typeLbl = new JLabel("Sensor Type:");
        typeLbl.setBounds(50, 70, 100, 25);
        inputPanel.add(typeLbl);

        sensorType = new JTextField();
        sensorType.setBounds(160, 70, 200, 25);
        inputPanel.add(sensorType);

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
        String[] columns = {"ID", "Sensor Name", "Sensor Type", "Created At"};
        tableModel = new DefaultTableModel(columns, 0);
        sensorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(sensorTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadSensors();

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void loadSensors() {
        // Clear existing rows
        tableModel.setRowCount(0);
        
        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM sensors ORDER BY sensor_id ASC")) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("sensor_id"),
                    rs.getString("sensor_name"),
                    rs.getString("sensor_type"),
                    rs.getTimestamp("created_at")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading sensors: " + ex.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            saveSensor();
        } else if (e.getSource() == refreshBtn) {
            loadSensors();
        } else if (e.getSource() == deleteBtn) {
            deleteSensor();
        }
    }

    private void saveSensor() {
        String name = sensorName.getText().trim();
        String type = sensorType.getText().trim();
        
        if (name.isEmpty() || type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }
        
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO sensors(sensor_name, sensor_type) VALUES(?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, type);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sensor saved successfully!");
            
            // Clear fields and reload table
            sensorName.setText("");
            sensorType.setText("");
            loadSensors();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSensor() {
        int selectedRow = sensorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a sensor to delete!");
            return;
        }
        
        int sensorId = (int) tableModel.getValueAt(selectedRow, 0);
        String sensorName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete sensor: " + sensorName + "?",
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM sensors WHERE sensor_id = ?")) {
                ps.setInt(1, sensorId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sensor deleted successfully!");
                loadSensors();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting sensor: " + ex.getMessage());
            }
        }
    }
}