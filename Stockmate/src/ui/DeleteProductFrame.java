package ui;

import service.ProductService;

import javax.swing.*;
import java.awt.*;

public class DeleteProductFrame extends JFrame {

    private JTextField idField;

    public DeleteProductFrame() {

        setTitle("Delete Product");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(240, 242, 245));

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(new Color(240, 242, 245));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(400, 200));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Delete Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formPanel.add(idField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        deleteButton.setForeground(Color.GRAY);
        deleteButton.setFocusPainted(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(deleteButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        wrapper.add(card);
        add(wrapper, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> deleteProduct());

        setVisible(true);
    }

    private void deleteProduct() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Product ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            ProductService service = new ProductService();
            boolean deleted = service.deleteProduct(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Product Deleted Successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Product Not Found");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID");
        }
    }
}