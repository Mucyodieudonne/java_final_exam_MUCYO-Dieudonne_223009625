package com.IoT;

import javax.swing.*;

public class Test_IoT extends JFrame {
    public Test_IoT() {
        setTitle("Internet of Things Assistant System");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardForm());
        tabs.addTab("Devices", new DeviceForm());
        tabs.addTab("Sensors", new SensorForm());
        tabs.addTab("Sensor Data", new SensorDataForm());
        tabs.addTab("Alerts", new AlertsForm());
        tabs.addTab("Logs", new LogsForm());

        add(tabs);
        setVisible(true);
    }
}
