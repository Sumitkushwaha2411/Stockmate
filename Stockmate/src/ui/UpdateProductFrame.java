package ui;

import service.ProductService;

import javax.swing.*;
import java.awt.*;

public class UpdateProductFrame extends JFrame {

    private JTextField idField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;

    public UpdateProductFrame() {

        setTitle("Update Product");
        setSize(540, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Outer background
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(440, 330));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Update Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel idLabel = new JLabel("Product ID:");
        JLabel nameLabel = new JLabel("New Name:");
        JLabel priceLabel = new JLabel("New Price:");
        JLabel quantityLabel = new JLabel("New Quantity:");

        idLabel.setFont(font);
        nameLabel.setFont(font);
        priceLabel.setFont(font);
        quantityLabel.setFont(font);

        idField = new JTextField();
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        idField.setFont(font);
        nameField.setFont(font);
        priceField.setFont(font);
        quantityField.setFont(font);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(idField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(nameField, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(priceField, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(quantityLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(quantityField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON =====
        JButton updateButton = new JButton("Update Product");
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        updateButton.setForeground(new Color(120, 120, 120));
        updateButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.add(updateButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        updateButton.addActionListener(e -> updateProduct());

        setVisible(true);
    }

    private void updateProduct() {

        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Product ID is required!");
            return;
        }

        try {

            int id = Integer.parseInt(idText);
            Double price = priceText.isEmpty() ? null : Double.parseDouble(priceText);
            Integer quantity = quantityText.isEmpty() ? null : Integer.parseInt(quantityText);

            ProductService service = new ProductService();
            boolean success = service.updateProduct(id, name, price, quantity);

            if (success) {
                JOptionPane.showMessageDialog(this, "Product Updated Successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Product not found!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid numeric value!");
        }
    }
}