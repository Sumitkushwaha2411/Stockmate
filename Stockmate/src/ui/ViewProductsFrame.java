package ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import service.ProductService;

public class ViewProductsFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;

    public ViewProductsFrame() {

        setTitle("View Products");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        getContentPane().setBackground(Theme.BACKGROUND);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(650, 380));
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JLabel title = new JLabel("Product Inventory", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));

        card.add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Price", "Quantity"};

        // ✅ Use class-level variables (NO local redeclaration)
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,15,15,15));

        card.add(scrollPane, BorderLayout.CENTER);

        add(card);

        //  Load data here
        loadProducts();

        setVisible(true);
    }

    public void loadProducts() {

        model.setRowCount(0);

        ProductService ps = new ProductService();
        List<Object[]> products = ps.getAllProducts();

        for (Object[] row : products) {
            model.addRow(row);
        }
    }

    private void styleTable(JTable table) {

        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionBackground(Theme.ACCENT);
        table.setGridColor(new Color(230,230,230));
        table.setShowVerticalLines(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.PRIMARY);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
    }
}