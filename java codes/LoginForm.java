package com.IoT;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginForm() {
        setTitle("IoT Assistant System - Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 25);
        add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 25);
        add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 25);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 150, 25);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 150, 100, 30);
        loginBtn.addActionListener(this);
        add(loginBtn);

        setVisible(true);
    }

   
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                     "SELECT user_id, full_name, role FROM users WHERE full_name=? AND password=?")) {

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");

                    JOptionPane.showMessageDialog(this,
                            "Welcome " + role.toUpperCase() + ": " + username + "!");

                    dispose();  // close login form

                    // Open dashboard with the role
                    new DashboardForm(role);

                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }



   /*** public static void main(String[] args) {
        new LoginForm();
    }***/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm());
    }

}
