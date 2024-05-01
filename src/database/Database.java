package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private final String URL = "jdbc:mysql://localhost:3306/FinalProject";
    private final String Kode = "root";
    private final String Nama = "";
    private static Database instance;
    private Connection con;

    private Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, Kode, Nama);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return con.prepareStatement(query);
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
}