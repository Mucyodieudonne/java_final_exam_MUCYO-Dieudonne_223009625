

package com.IoT;

import javax.swing.*;
import java.awt.*;

public class DashboardForm extends JFrame {

    private String userRole;

    // No-arg constructor — kept for backward compatibility.
    // It treats caller as a normal "user".
    public DashboardForm() {
        this("user"); // default to 'user' role if none provided
    }

    // New constructor that accepts role
    public DashboardForm(String role) {
        this.userRole = (role == null) ? "user" : role.toLowerCase();

        setTitle("IoT Assistant System - (" + this.userRole.toUpperCase() + ")");
        setBounds(100, 100, 1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Admin-only tab
        if (this.userRole.equalsIgnoreCase("admin")) {
            tabbedPane.addTab("User Management", createUserManagementPanel());
        }

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Devices", createDevicesPanel());
        tabbedPane.addTab("Sensors", createSensorsPanel());
        tabbedPane.addTab("Sensor Data", createSensorDataPanel());
        tabbedPane.addTab("Alerts", createAlertsPanel());
        tabbedPane.addTab("Logs", createLogsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // --- keep your existing helper methods below (createDashboardPanel etc.) ---
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 20, 20));

        JButton devicesBtn = new JButton("Manage Devices");
        JButton sensorsBtn = new JButton("Manage Sensors");
        JButton dataBtn = new JButton("View Sensor Data");
        JButton alertsBtn = new JButton("View Alerts");
        JButton logsBtn = new JButton("View Logs");
        JButton exitBtn = new JButton("Exit");

        devicesBtn.addActionListener(e -> new DeviceForm());
        sensorsBtn.addActionListener(e -> new SensorForm());
        dataBtn.addActionListener(e -> new SensorDataForm());
        alertsBtn.addActionListener(e -> new AlertsForm());
        logsBtn.addActionListener(e -> new LogsForm());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(devicesBtn);
        panel.add(sensorsBtn);
        panel.add(dataBtn);
        panel.add(alertsBtn);
        panel.add(logsBtn);
        panel.add(exitBtn);

        return panel;
    }

    private JPanel createDevicesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton openBtn = new JButton("Open Devices Manager");
        openBtn.addActionListener(e -> new DeviceForm());
        panel.add(openBtn, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createSensorsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton openBtn = new JButton("Open Sensors Manager");
        openBtn.addActionListener(e -> new SensorForm());
        panel.add(openBtn, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createSensorDataPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton openBtn = new JButton("Open Sensor Data Viewer");
        openBtn.addActionListener(e -> new SensorDataForm());
        panel.add(openBtn, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton openBtn = new JButton("Open Alerts Manager");
        openBtn.addActionListener(e -> new AlertsForm());
        panel.add(openBtn, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createLogsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton openBtn = new JButton("Open Logs Viewer");
        openBtn.addActionListener(e -> new LogsForm());
        panel.add(openBtn, BorderLayout.NORTH);
        return panel;
    }

   /*** private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Admin: Manage Users Here"));

        JButton addUserBtn = new JButton("Add User");
       

        addUserBtn.addActionListener(e -> new UserForm());
      

        panel.add(addUserBtn);

        return panel;
    }***/
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JLabel label = new JLabel("Admin: Manage Users Here");
        label.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(label, BorderLayout.NORTH);

        JButton addUserBtn = new JButton("Open User Management");
        addUserBtn.addActionListener(e -> new UserForm());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addUserBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

}
