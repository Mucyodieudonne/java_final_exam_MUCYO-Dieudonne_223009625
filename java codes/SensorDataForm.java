

package com.IoT;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SensorDataForm extends JFrame implements ActionListener {
    JTable table;
    DefaultTableModel model;
    JButton refreshBtn, deleteBtn;
    
    public SensorDataForm() {
        setTitle("Sensor Data Viewer");
        setBounds(150, 150, 900, 600);
        setLayout(new BorderLayout());
        
        // Top Panel with buttons
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(this);
        topPanel.add(refreshBtn);
        
        deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(this);
        topPanel.add(deleteBtn);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Table columns (removed Unit column)
        model = new DefaultTableModel(new String[]{"Data ID", "Sensor ID", "Sensor Name", "Value", "Recorded At"}, 0);
        table = new JTable(model);
        
        // Scroll pane
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, BorderLayout.CENTER);
        
        // Load data from database
        loadData();
        
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadData() {
        // Clear existing rows
        model.setRowCount(0);
        
        String query = "SELECT sd.data_id, sd.sensor_id, s.sensor_name, sd.value, sd.recorded_at " +
                       "FROM sensor_data sd " +
                       "JOIN sensors s ON sd.sensor_id = s.sensor_id " +
                       "ORDER BY sd.data_id ASC";
        
        try (Connection conn = DB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("data_id"),
                    rs.getInt("sensor_id"),
                    rs.getString("sensor_name"),
                    rs.getString("value"),
                    rs.getTimestamp("recorded_at")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshBtn) {
            loadData();
            JOptionPane.showMessageDialog(this, "Data refreshed successfully!");
        } else if (e.getSource() == deleteBtn) {
            deleteData();
        }
    }
    
    private void deleteData() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete!");
            return;
        }
        
        int dataId = (int) model.getValueAt(selectedRow, 0);
        String sensorName = (String) model.getValueAt(selectedRow, 2);
        String value = (String) model.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this record?\nSensor: " + sensorName + "\nValue: " + value,
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM sensor_data WHERE data_id = ?")) {
                ps.setInt(1, dataId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting record: " + ex.getMessage());
            }
        }
    }
}