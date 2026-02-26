package service;

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public boolean addProduct(String name, double price, int quantity) {

        String sql = "INSERT INTO products (product_name, price, quantity) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean updateProduct(int id, String name, Double price, Integer quantity) {

        StringBuilder sql = new StringBuilder("UPDATE products SET ");
        boolean first = true;

        if (name != null && !name.isEmpty()) {
            sql.append("product_name=?");
            first = false;
        }

        if (price != null) {
            if (!first) sql.append(", ");
            sql.append("price=?");
            first = false;
        }

        if (quantity != null) {
            if (!first) sql.append(", ");
            sql.append("quantity=?");
        }

        sql.append(" WHERE product_id=?");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;

            if (name != null && !name.isEmpty())
                ps.setString(index++, name);

            if (price != null)
                ps.setDouble(index++, price);

            if (quantity != null)
                ps.setInt(index++, quantity);

            ps.setInt(index, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int id) {

        String deleteSales = "DELETE FROM sales WHERE product_id=?";
        String deleteProduct = "DELETE FROM products WHERE product_id=?";

        Connection con = null;

        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            // Delete related sales first (avoid FK constraint error)
            try (PreparedStatement ps1 = con.prepareStatement(deleteSales)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            int rows;
            try (PreparedStatement ps2 = con.prepareStatement(deleteProduct)) {
                ps2.setInt(1, id);
                rows = ps2.executeUpdate();
            }

            con.commit();
            return rows > 0;

        } catch (Exception e) {

            try {
                if (con != null)
                    con.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {
            try {
                if (con != null)
                    con.setAutoCommit(true);
            } catch (Exception ignored) {}
        }
    }

  
    public List<Object[]> getAllProducts() {

        List<Object[]> products = new ArrayList<>();

        String sql = """
                SELECT product_id, product_name, price, quantity
                FROM products
                ORDER BY product_id
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                products.add(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public Object[] getProductById(int id) {

        String sql = """
                SELECT product_id, product_name, price, quantity
                FROM products
                WHERE product_id=?
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllProductNames() {

        List<String> names = new ArrayList<>();

        String sql = "SELECT product_name FROM products ORDER BY product_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("product_name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }

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

    public double getProductPrice(int id) {

        String sql = "SELECT price FROM products WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return rs.getDouble("price");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public boolean updateProductQuantity(int id, int newQuantity) {

        String sql = "UPDATE products SET quantity=? WHERE product_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, newQuantity);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}