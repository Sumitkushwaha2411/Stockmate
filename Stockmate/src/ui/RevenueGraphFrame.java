package ui;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Map;
import javax.swing.*;
import service.SalesService;

public class RevenueGraphFrame extends JFrame {

    private Map<String, Double> revenueData;
    private JComboBox<String> filterBox;
    private ChartPanel chartPanel;

    public RevenueGraphFrame() {

        setTitle("Revenue Analytics");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Top Filter Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        topPanel.setBackground(Color.WHITE);

        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        filterBox = new JComboBox<>(new String[]{"Daily", "Monthly", "Yearly"});
        filterBox.setPreferredSize(new Dimension(150, 32));
        filterBox.setFont(new Font("SansSerif", Font.PLAIN, 14));

        topPanel.add(filterLabel);
        topPanel.add(filterBox);

        add(topPanel, BorderLayout.NORTH);

        chartPanel = new ChartPanel();
        add(chartPanel, BorderLayout.CENTER);

        loadData("Monthly");

        filterBox.addActionListener(e ->
                loadData((String) filterBox.getSelectedItem())
        );

        setVisible(true);
    }

    private void loadData(String type) {
        SalesService ss = new SalesService();
        revenueData = ss.getRevenueData(type);
        chartPanel.repaint();
    }

    
    class ChartPanel extends JPanel {

        private final DecimalFormat df = new DecimalFormat("#,###");

        // Professional Dashboard Palette
        private final Color[] barColors = {
                new Color(41, 128, 185),   // Royal Blue
                new Color(39, 174, 96),    // Emerald
                new Color(231, 76, 60),    // Alizarin
                new Color(155, 89, 182),   // Amethyst
                new Color(243, 156, 18),   // Orange
                new Color(26, 188, 156),   // Turquoise
                new Color(52, 73, 94),     // Dark Blue Gray
                new Color(192, 57, 43)     // Dark Red
        };

        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            setBackground(Color.WHITE);

            int width = getWidth();
            int height = getHeight();

            int leftMargin = 110;
            int rightMargin = 60;
            int topMargin = 90;
            int bottomMargin = 110;

            int chartWidth = width - leftMargin - rightMargin;
            int chartHeight = height - topMargin - bottomMargin;

            if (revenueData == null || revenueData.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
                g2.drawString("No Data Available",
                        width / 2 - 70,
                        height / 2);
                return;
            }

            double maxRevenue = revenueData.values()
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(1);

            // ===== GRID LINES =====
            g2.setColor(new Color(235, 235, 235));
            int gridLines = 5;

            for (int i = 0; i <= gridLines; i++) {

                int y = topMargin + (i * chartHeight / gridLines);
                g2.drawLine(leftMargin, y,
                        width - rightMargin, y);

                double value = maxRevenue -
                        (i * maxRevenue / gridLines);

                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("₹" + df.format(value),
                        35,
                        y + 5);

                g2.setColor(new Color(235, 235, 235));
            }

            // ===== AXIS =====
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(leftMargin, topMargin,
                    leftMargin, height - bottomMargin);

            g2.drawLine(leftMargin, height - bottomMargin,
                    width - rightMargin, height - bottomMargin);

            int numberOfBars = revenueData.size();
            int gap = 35;

            int barWidth = Math.min(90,
                    (chartWidth / numberOfBars) - gap);

            int totalBarArea = numberOfBars * barWidth +
                    (numberOfBars - 1) * gap;

            int startX = leftMargin +
                    (chartWidth - totalBarArea) / 2;

            int x = startX;
            int colorIndex = 0;

            for (Map.Entry<String, Double> entry : revenueData.entrySet()) {

                double value = entry.getValue();

                int barHeight = (int)
                        ((value / maxRevenue) * chartHeight);

                int y = height - bottomMargin - barHeight;

                // Different professional color per bar
                g2.setColor(barColors[colorIndex % barColors.length]);
                g2.fillRoundRect(x, y,
                        barWidth, barHeight, 15, 15);

                // Value label
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString("₹" + df.format(value),
                        x + barWidth / 4,
                        y - 10);

                // X label
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString(entry.getKey(),
                        x + barWidth / 5,
                        height - bottomMargin + 30);

                x += barWidth + gap;
                colorIndex++;
            }

            // ===== TITLE =====
            g2.setFont(new Font("SansSerif", Font.BOLD, 24));
            g2.setColor(Color.BLACK);
            g2.drawString("Revenue Analytics",
                    width / 2 - 130,
                    55);
        }
    }
}