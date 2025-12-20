
package com.IoT;
import com.IoT.DB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class LogsForm extends JFrame implements ActionListener {
    JTable logTable;
    JTextField deviceIdTxt = new JTextField();
    JTextField actionTxt = new JTextField();
    JTextField userIdTxt = new JTextField();
    JButton addbtn = new JButton("Add Log");
    JButton refreshbtn = new JButton("Refresh");
    JButton closebtn = new JButton("Close");
    
    public LogsForm() {
        setTitle("IoT Assistant System - Logs");
        setBounds(100, 100, 900, 600);
        setLayout(null);
        
        // Table
        String[] columnNames = {"Log ID", "Device ID", "Action", "Performed By", "Timestamp"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        logTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setBounds(20, 20, 850, 400);
        add(scrollPane);
        
        // Labels
        JLabel deviceLabel = new JLabel("Device ID:");
        deviceLabel.setBounds(40, 440, 100, 30);
        add(deviceLabel);
        
        JLabel actionLabel = new JLabel("Action:");
        actionLabel.setBounds(40, 480, 100, 30);
        add(actionLabel);
        
        JLabel userLabel = new JLabel("User ID:");
        userLabel.setBounds(450, 440, 100, 30);
        add(userLabel);
        
        // Input fields
        deviceIdTxt.setBounds(140, 440, 250, 30);
        actionTxt.setBounds(140, 480, 250, 30);
        userIdTxt.setBounds(550, 440, 250, 30);
        
        add(deviceIdTxt);
        add(actionTxt);
        add(userIdTxt);
        
        // Buttons
        addbtn.setBounds(140, 520, 120, 30);
        refreshbtn.setBounds(280, 520, 120, 30);
        closebtn.setBounds(420, 520, 120, 30);
        
        add(addbtn);
        add(refreshbtn);
        add(closebtn);
        
        addbtn.addActionListener(this);
        refreshbtn.addActionListener(this);
        closebtn.addActionListener(this);
        
        loadLogs();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void loadLogs() {
        try (Connection con = DB.getConnection()) {
            String sql = "SELECT log_id, device_id, action, performed_by, timestamp FROM logs ORDER BY timestamp DESC";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            DefaultTableModel model = (DefaultTableModel) logTable.getModel();
            model.setRowCount(0); // clear existing rows
            
            while (rs.next()) {
                int logId = rs.getInt("log_id");
                int deviceId = rs.getInt("device_id");
                String action = rs.getString("action");
                int performedBy = rs.getInt("performed_by");
                String timestamp = rs.getString("timestamp");
                
                model.addRow(new Object[]{logId, deviceId, action, performedBy, timestamp});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading logs: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void addLog() {
        try {
            if (deviceIdTxt.getText().trim().isEmpty() || 
                actionTxt.getText().trim().isEmpty() || 
                userIdTxt.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            
            try (Connection con = DB.getConnection()) {
                String sql = "INSERT INTO logs (device_id, action, performed_by) VALUES (?, ?, ?)";
                PreparedStatement stm = con.prepareStatement(sql);
                stm.setInt(1, Integer.parseInt(deviceIdTxt.getText()));
                stm.setString(2, actionTxt.getText());
                stm.setInt(3, Integer.parseInt(userIdTxt.getText()));
                
                int inserted = stm.executeUpdate();
                if (inserted > 0) {
                    JOptionPane.showMessageDialog(this, "Log added successfully!");
                    deviceIdTxt.setText("");
                    actionTxt.setText("");
                    userIdTxt.setText("");
                    loadLogs();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add log!");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Device ID and User ID must be numbers!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addbtn) {
            addLog();
        } else if (e.getSource() == refreshbtn) {
            loadLogs();
        } else if (e.getSource() == closebtn) {
            dispose();
        }
    }
}