package ui;

import java.awt.*;
import javax.swing.*;
import service.SalesService;

public class RecordSaleFrame extends JFrame {

    private JTextField productIdField;
    private JTextField quantityField;
    private DashboardFrame dashboard;

    public RecordSaleFrame(DashboardFrame dashboard) {

        this.dashboard = dashboard;

        setTitle("Record Sale");
        setSize(520, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Outer background (same as other frames)
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

        // White card
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(420, 260));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Title
        JLabel title = new JLabel("Record New Sale");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel productLabel = new JLabel("Product ID:");
        JLabel quantityLabel = new JLabel("Quantity:");

        productLabel.setFont(font);
        quantityLabel.setFont(font);

        productIdField = new JTextField();
        quantityField = new JTextField();

        productIdField.setFont(font);
        quantityField.setFont(font);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(productLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(productIdField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(quantityLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(quantityField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JButton recordBtn = new JButton("Record Sale");
        recordBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        recordBtn.setForeground(new Color(120, 120, 120));
        recordBtn.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        buttonPanel.add(recordBtn);

        card.add(buttonPanel, BorderLayout.SOUTH);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        recordBtn.addActionListener(e -> handleSale());

        setVisible(true);
    }

    private void handleSale() {

        try {

            if (productIdField.getText().trim().isEmpty() ||
                quantityField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            int productId = Integer.parseInt(productIdField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0");
                return;
            }

            SalesService ss = new SalesService();
            boolean success = ss.recordSale(productId, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Sale Recorded Successfully");

                if (dashboard != null) {
                    dashboard.refreshDashboard();
                }

                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Not enough stock or invalid product ID");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values");
        }
    }
}