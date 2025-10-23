package org.example.sistemalogin.Models;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver no encontrado", e);
        }

        String url = "Base de datos";
        String user = "NombreUsuarios";
        String password = "Tu contra";

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("Proceso completo, no hubo error al sincronizar operacion");
        return connection;
    }
}