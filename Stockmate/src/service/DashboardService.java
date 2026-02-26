package service;

import database.DBConnection;
import java.sql.*;

public class DashboardService {

    public int getTotalProducts() {

        String sql = "SELECT COUNT(*) FROM products";

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

    public double getTotalRevenue() {

        String sql = "SELECT SUM(total_amount) FROM sales";

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

    public double getTodayRevenue() {

        String sql = """
                SELECT SUM(total_amount)
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
                SELECT SUM(total_amount)
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
    public int getTotalSales() {

        String sql = "SELECT COUNT(*) FROM sales";

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
}