package ui;

import java.awt.*;
import javax.swing.*;
import service.DashboardService;

public class DashboardFrame extends JFrame {

    private JLabel totalProductsLabel;
    private JLabel totalSalesLabel;
    private JLabel totalRevenueLabel;
    private JLabel todayRevenueLabel;
    private JLabel monthlyRevenueLabel;
    private JLabel lowStockLabel;

    private JPanel statsPanel;

    public DashboardFrame() {

        setTitle("StockMate Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ======================
        // STATS PANEL (PHASE 7)
        // ======================

        statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        totalProductsLabel = new JLabel();
        totalSalesLabel = new JLabel();
        totalRevenueLabel = new JLabel();
        todayRevenueLabel = new JLabel();
        monthlyRevenueLabel = new JLabel();
        lowStockLabel = new JLabel();

        statsPanel.add(createCard("Total Products", totalProductsLabel));
        statsPanel.add(createCard("Total Sales", totalSalesLabel));
        statsPanel.add(createCard("Total Revenue", totalRevenueLabel));
        statsPanel.add(createCard("Today's Revenue", todayRevenueLabel));
        statsPanel.add(createCard("Monthly Revenue", monthlyRevenueLabel));
        statsPanel.add(createCard("Low Stock Items", lowStockLabel));

        add(statsPanel, BorderLayout.NORTH);

        // ======================
        // LEFT MENU
        // ======================

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(8, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addProductBtn = new JButton("Add Product");
        JButton viewProductBtn = new JButton("View Products");
        JButton updateProductBtn = new JButton("Update Product");
        JButton deleteProductBtn = new JButton("Delete Product");
        JButton recordSaleBtn = new JButton("Record Sale");
        JButton salesReportBtn = new JButton("Sales Report");
        JButton revenueGraphBtn = new JButton("Revenue Graph");
        JButton logoutBtn = new JButton("Logout");

        menuPanel.add(addProductBtn);
        menuPanel.add(viewProductBtn);
        menuPanel.add(updateProductBtn);
        menuPanel.add(deleteProductBtn);
        menuPanel.add(recordSaleBtn);
        menuPanel.add(salesReportBtn);
        menuPanel.add(revenueGraphBtn);
        menuPanel.add(logoutBtn);

        add(menuPanel, BorderLayout.WEST);

       

        JLabel welcome = new JLabel("Welcome to StockMate Dashboard", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(welcome, BorderLayout.CENTER);

       

        recordSaleBtn.addActionListener(e -> new RecordSaleFrame(this));
        addProductBtn.addActionListener(e -> new AddProductFrame());
        viewProductBtn.addActionListener(e -> new ViewProductsFrame());
        updateProductBtn.addActionListener(e -> new UpdateProductFrame());
        deleteProductBtn.addActionListener(e -> new DeleteProductFrame());
        salesReportBtn.addActionListener(e -> new SalesReportFrame());
        revenueGraphBtn.addActionListener(e -> new RevenueGraphFrame());

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        refreshDashboard();
        setVisible(true);
    }

  

    public void refreshDashboard() {

        DashboardService ds = new DashboardService();

        totalProductsLabel.setText(String.valueOf(ds.getTotalProducts()));
        totalSalesLabel.setText(String.valueOf(ds.getTotalSales()));
        totalRevenueLabel.setText("₹ " + ds.getTotalRevenue());
        todayRevenueLabel.setText("₹ " + ds.getTodayRevenue());
        monthlyRevenueLabel.setText("₹ " + ds.getMonthlyRevenue());
        lowStockLabel.setText(String.valueOf(ds.getLowStockCount()));

        // Highlight low stock in red
        if (ds.getLowStockCount() > 0) {
            lowStockLabel.setForeground(Color.RED);
        } else {
            lowStockLabel.setForeground(new Color(0, 128, 0));
        }
    }


    private JPanel createCard(String title, JLabel valueLabel) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(new Color(0, 128, 0));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }
}