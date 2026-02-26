package ui;

import java.awt.*;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import service.ProductService;
import service.SalesService;

public class SalesReportFrame extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    private JLabel revenueLabel;

    private JComboBox<String> productDropdown;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;

    public SalesReportFrame() {

        setTitle("Sales Report");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Theme.BACKGROUND);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Sales Report", SwingConstants.CENTER);
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(15,10,10,10));
        topPanel.add(title, BorderLayout.NORTH);

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        filterBar.setBackground(Color.WHITE);
        filterBar.setBorder(BorderFactory.createEmptyBorder(5,20,10,20));

        filterBar.add(new JLabel("Product:"));

        productDropdown = new JComboBox<>();
        productDropdown.setPreferredSize(new Dimension(180, 30));
        filterBar.add(productDropdown);

        filterBar.add(new JLabel("From:"));

        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        fromDateSpinner.setEditor(new JSpinner.DateEditor(fromDateSpinner, "yyyy-MM-dd"));
        fromDateSpinner.setPreferredSize(new Dimension(140, 30));
        filterBar.add(fromDateSpinner);

        filterBar.add(new JLabel("To:"));

        toDateSpinner = new JSpinner(new SpinnerDateModel());
        toDateSpinner.setEditor(new JSpinner.DateEditor(toDateSpinner, "yyyy-MM-dd"));
        toDateSpinner.setPreferredSize(new Dimension(140, 30));
        filterBar.add(toDateSpinner);

        JButton filterBtn = new JButton("Apply Filter");
        styleButton(filterBtn);
        filterBar.add(filterBtn);

        topPanel.add(filterBar, BorderLayout.SOUTH);
        card.add(topPanel, BorderLayout.NORTH);

        String[] columns = {
                "Sale ID",
                "Product Name",
                "Quantity",
                "Total Amount",
                "Sale Date"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        card.add(scrollPane, BorderLayout.CENTER);

        revenueLabel = new JLabel("Total Revenue: ₹ 0");
        revenueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        revenueLabel.setFont(Theme.NORMAL_FONT);
        revenueLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,20));

        card.add(revenueLabel, BorderLayout.SOUTH);

        add(card);

        loadProductsIntoDropdown();
        loadSalesData();

        filterBtn.addActionListener(e -> applyFilter());

        setVisible(true);
    }

    private void loadProductsIntoDropdown() {

        productDropdown.addItem("All");

        ProductService ps = new ProductService();
        List<Object[]> products = ps.getAllProducts();

        for (Object[] row : products) {
            productDropdown.addItem(row[1].toString());
        }
    }

    private void applyFilter() {

        String selectedProduct = productDropdown.getSelectedItem().toString();

        if ("All".equals(selectedProduct)) {
            selectedProduct = null;
        }

        Date utilFromDate = (Date) fromDateSpinner.getValue();
        Date utilToDate = (Date) toDateSpinner.getValue();

        java.sql.Date fromDate = new java.sql.Date(utilFromDate.getTime());
        java.sql.Date toDate = new java.sql.Date(utilToDate.getTime());

        SalesService ss = new SalesService();
        List<Object[]> sales = ss.getFilteredSales(selectedProduct, fromDate, toDate);

        model.setRowCount(0);

        double totalRevenue = 0;

        for (Object[] row : sales) {
            model.addRow(row);
            if (row[3] != null) {
                totalRevenue += ((Number) row[3]).doubleValue();
            }
        }

        revenueLabel.setText("Total Revenue: ₹ " + totalRevenue);
    }

    private void loadSalesData() {

        model.setRowCount(0);

        SalesService ss = new SalesService();
        List<Object[]> sales = ss.getAllSales();

        double totalRevenue = 0;

        for (Object[] row : sales) {
            model.addRow(row);
            if (row[3] != null) {
                totalRevenue += ((Number) row[3]).doubleValue();
            }
        }

        revenueLabel.setText("Total Revenue: ₹ " + totalRevenue);
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

    private void styleButton(JButton button) {

        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));

        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);

        button.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(18),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private static class RoundedBorder implements javax.swing.border.Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(5, 10, 5, 10);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(220,220,220));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}