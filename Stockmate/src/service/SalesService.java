package service;

import database.DBConnection;
import java.sql.*;
import java.util.*;

public class SalesService {

    public boolean recordSale(int productId, int quantity) {

        String checkSql = "SELECT quantity, price FROM products WHERE product_id = ?";
        String insertSql = "INSERT INTO sales (product_id, quantity_sold, total_amount) VALUES (?, ?, ?)";
        String updateSql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ?";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {

                checkPs.setInt(1, productId);
                ResultSet rs = checkPs.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    return false;
                }

                int availableQty = rs.getInt("quantity");
                double price = rs.getDouble("price");

                if (quantity <= 0 || quantity > availableQty) {
                    con.rollback();
                    return false;
                }

                double totalAmount = price * quantity;

                try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {

                    insertPs.setInt(1, productId);
                    insertPs.setInt(2, quantity);
                    insertPs.setDouble(3, totalAmount);
                    insertPs.executeUpdate();
                }

                try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {

                    updatePs.setInt(1, quantity);
                    updatePs.setInt(2, productId);
                    updatePs.executeUpdate();
                }

                con.commit();
                return true;
            }

        } catch (Exception e) {

            try {
                if (con != null)
                    con.rollback();
            } catch (Exception ignored) {}

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null)
                    con.setAutoCommit(true);
            } catch (Exception ignored) {}
        }
    }

    public List<Object[]> getAllSales() {

        List<Object[]> list = new ArrayList<>();

        String sql = """
                SELECT s.sale_id,
                       p.product_name,
                       s.quantity_sold,
                       s.total_amount,
                       s.sale_date
                FROM sales s
                JOIN products p ON s.product_id = p.product_id
                ORDER BY s.sale_date DESC
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                list.add(new Object[]{
                        rs.getInt("sale_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("sale_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Object[]> getFilteredSales(String productName,
                                           java.util.Date fromDate,
                                           java.util.Date toDate) {

        List<Object[]> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT s.sale_id,
                   p.product_name,
                   s.quantity_sold,
                   s.total_amount,
                   s.sale_date
            FROM sales s
            JOIN products p ON s.product_id = p.product_id
            WHERE 1=1
        """);

        if (productName != null && !productName.equals("All")) {
            sql.append(" AND p.product_name = ?");
        }

        if (fromDate != null) {
            sql.append(" AND DATE(s.sale_date) >= ?");
        }

        if (toDate != null) {
            sql.append(" AND DATE(s.sale_date) <= ?");
        }

        sql.append(" ORDER BY s.sale_date DESC");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            if (productName != null && !productName.equals("All")) {
                ps.setString(index++, productName);
            }

            if (fromDate != null) {
                ps.setDate(index++, new java.sql.Date(fromDate.getTime()));
            }

            if (toDate != null) {
                ps.setDate(index++, new java.sql.Date(toDate.getTime()));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Object[]{
                        rs.getInt("sale_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity_sold"),
                        rs.getDouble("total_amount"),
                        rs.getTimestamp("sale_date")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public double getTodayRevenue() {

        String sql = """
                SELECT IFNULL(SUM(total_amount),0)
                FROM sales
                WHERE DATE(sale_date) = CURDATE()
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

   
    public double getMonthlyRevenue() {

        String sql = """
                SELECT IFNULL(SUM(total_amount),0)
                FROM sales
                WHERE MONTH(sale_date) = MONTH(CURDATE())
                AND YEAR(sale_date) = YEAR(CURDATE())
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getLowStockCount() {

        String sql = "SELECT COUNT(*) FROM products WHERE quantity < 5";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
   
    public Map<String, Double> getRevenueData(String type) {

            Map<String, Double> data = new LinkedHashMap<>();
            String sql = "";

            if ("Daily".equals(type)) {

                sql = """
                    SELECT DATE(sale_date) AS label,
                        SUM(total_amount) AS revenue
                    FROM sales
                    GROUP BY DATE(sale_date)
                    ORDER BY DATE(sale_date)
                """;

            } else if ("Monthly".equals(type)) {

                sql = """
                    SELECT DATE_FORMAT(sale_date, '%Y-%m') AS label,
                        SUM(total_amount) AS revenue
                    FROM sales
                    GROUP BY DATE_FORMAT(sale_date, '%Y-%m')
                    ORDER BY label
                """;

            } else if ("Yearly".equals(type)) {

                sql = """
                    SELECT YEAR(sale_date) AS label,
                        SUM(total_amount) AS revenue
                    FROM sales
                    GROUP BY YEAR(sale_date)
                    ORDER BY YEAR(sale_date)
                """;
            }

            try (Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    data.put(
                            rs.getString("label"),
                            rs.getDouble("revenue")
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
    }
}