package ui;

import database.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    public LoginFrame() {

        setTitle("StockMate Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Welcome to StockMate");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(90, 25, 250, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setBounds(50, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 180, 30);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setBounds(50, 125, 100, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 125, 180, 30);
        panel.add(passwordField);

        showPassword = new JCheckBox("Show Password");
        showPassword.setBounds(150, 160, 150, 25);
        showPassword.setBackground(new Color(245, 245, 245));
        panel.add(showPassword);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 200, 120, 35);
        loginBtn.setBackground(new Color(52, 152, 219));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        panel.add(loginBtn);

        add(panel);

        showPassword.addActionListener(e -> {

            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }

        });

        loginBtn.addActionListener(e -> loginUser());

        setVisible(true);
    }

    private void loginUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password"
            );

            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM admin WHERE username=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Login Successful"
                );

                DashboardFrame dashboard = new DashboardFrame();
                dashboard.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Connection Error: " + ex.getMessage()
            );
        }
    }
}