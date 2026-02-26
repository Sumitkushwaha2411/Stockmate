package ui;

import java.awt.*;
import javax.swing.*;
import service.ProductService;

public class AddProductFrame extends JFrame {

    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;

    public AddProductFrame() {

        setTitle("Add Product");
        setSize(520, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

      
        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

       
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(420, 300));
        card.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

       
        JLabel title = new JLabel("Add New Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

       
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JLabel nameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");

        nameLabel.setFont(font);
        priceLabel.setFont(font);
        quantityLabel.setFont(font);

        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();

        nameField.setFont(font);
        priceField.setFont(font);
        quantityField.setFont(font);

       
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(nameField, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(priceLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(priceField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(quantityLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        formPanel.add(quantityField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Product");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        addButton.setForeground(new Color(120, 120, 120));
        addButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.add(addButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        addButton.addActionListener(e -> addProduct());

        setVisible(true);
    }

    private void addProduct() {

        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        try {

            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);

            ProductService service = new ProductService();
            service.addProduct(name, price, quantity);

            JOptionPane.showMessageDialog(this, "Product Added Successfully!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format!");
        }
    }
}