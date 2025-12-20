package com.IoT;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UserForm extends JFrame implements ActionListener {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton addBtn, refreshBtn, deleteBtn;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserForm() {
        setTitle("User Management");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Top Panel: Add User Form ---
     // formPanel: contains your form fields
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add User"));

        // wrapper panel: controls the position of formPanel
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // aligns child to right
        wrapper.add(formPanel);

        // add wrapper to the top of JFrame
        add(wrapper, BorderLayout.NORTH);


        formPanel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"user", "admin"});
        formPanel.add(roleBox);

        addBtn = new JButton("Add User");
        addBtn.addActionListener(this);
        formPanel.add(new JLabel()); // empty label for spacing
        formPanel.add(addBtn);

        add(formPanel, BorderLayout.NORTH);

        // --- Center Panel: User Table ---
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Users"));
        add(scrollPane, BorderLayout.CENTER);

        // Add table columns
        tableModel.addColumn("ID");
        tableModel.addColumn("Full Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Role");
        tableModel.addColumn("Created At");

        // --- Bottom Panel: Refresh and Delete Buttons ---
        JPanel bottomPanel = new JPanel();
        refreshBtn = new JButton("Refresh Table");
        refreshBtn.addActionListener(e -> loadUsers());
        bottomPanel.add(refreshBtn);

        deleteBtn = new JButton("Delete Selected User");
        deleteBtn.addActionListener(e -> deleteUser());
        bottomPanel.add(deleteBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load users initially
        loadUsers();

        setVisible(true);
    }

    private void loadUsers() {
        tableModel.setRowCount(0); // clear existing rows

        try (Connection conn = DB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT user_id, full_name, email, role, created_at FROM users")) {

            while (rs.next()) {
                Object[] row = new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0); // ID column

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {

                ps.setInt(1, userId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully!");
                    loadUsers(); // refresh table
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String fullName = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleBox.getSelectedItem();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            try (Connection conn = DB.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO users (full_name, email, password, role, created_at) VALUES (?, ?, ?, ?, NOW())")) {

                ps.setString(1, fullName);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, role);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "User added successfully!");
                    nameField.setText("");
                    emailField.setText("");
                    passwordField.setText("");
                    roleBox.setSelectedIndex(0);

                    loadUsers(); // refresh table
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add user.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }
}
