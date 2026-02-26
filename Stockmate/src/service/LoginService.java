package service;
import database.DBConnection;
import java.sql.*;


public class LoginService {

    public boolean authenticate(String username, String password) {

        try (Connection con = DBConnection.getConnection()) {

            String query = "SELECT * FROM admin WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}