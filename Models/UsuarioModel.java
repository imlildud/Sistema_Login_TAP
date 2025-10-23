package org.example.sistemalogin.Models;
import org.example.sistemalogin.Utils.*;
import java.sql.*;
import java.time.LocalDate;

public class UsuarioModel {

    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, correo, password_hash, nombre_completo, fecha_nacimiento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getCorreo());
            pstmt.setString(3, usuario.getPasswordHash());
            pstmt.setString(4, usuario.getNombreCompleto());
            pstmt.setDate(5, Date.valueOf(usuario.getFechaNacimiento()));

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error insertando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUsuario(String username) {
        String sql = "SELECT id FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error verificando usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeCorreo(String correo) {
        String sql = "SELECT id FROM usuarios WHERE correo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error verificando correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public Usuario obtenerUsuarioPorUsername(String username) {
        String sql = "SELECT id, username, correo, password_hash, nombre_completo, fecha_nacimiento, fecha_registro FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setPasswordHash(rs.getString("password_hash"));
                usuario.setNombreCompleto(rs.getString("nombre_completo"));
                usuario.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                usuario.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo usuario por username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean verificarCredenciales(String username, String password) {
        String sql = "SELECT password_hash FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return PasswordUtils.verifyPassword(password, storedHash);
            }

        } catch (SQLException e) {
            System.err.println("Error verificando credenciales: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public String obtenerNombreCompleto(String username) {
        String sql = "SELECT nombre_completo FROM usuarios WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("nombre_completo");
            }

        } catch (SQLException e) {
            System.err.println("Error obteniendo nombre completo: " + e.getMessage());
            e.printStackTrace();
        }
        return "Usuario";
    }
}