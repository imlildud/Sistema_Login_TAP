package org.example.sistemalogin.Controllers;
import org.example.sistemalogin.Main;
import org.example.sistemalogin.Models.Usuario;
import org.example.sistemalogin.Models.UsuarioModel;
import org.example.sistemalogin.Utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField pfPassword;
    @FXML private Label lblMensaje;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnIrARegistro;

    private final UsuarioModel usuarioModel;
    public LoginController() {
        this.usuarioModel = new UsuarioModel();
    }

    private Main main;
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        lblMensaje.setText("");

        btnIniciarSesion.setOnAction(e -> iniciarSesion());
        btnIrARegistro.setOnAction(e -> irARegistro());
    }

    @FXML
    private void iniciarSesion() {
        try {
            String username = txtUsername.getText();
            String password = pfPassword.getText();

            if (username == null || username.trim().isEmpty()) {
                mostrarError("El nombre de usuario es obligatorio.");
                return;
            }
            if (password == null || password.isEmpty()) {
                mostrarError("La contraseña es obligatoria.");
                return;
            }

            if (!usuarioModel.existeUsuario(username)) {
                mostrarError("El nombre de usuario no existe.");
                return;
            }

            Usuario usuario = usuarioModel.obtenerUsuarioPorUsername(username);
            if (usuario != null && PasswordUtils.verifyPassword(password, usuario.getPasswordHash())) {
                mostrarExito("Inicio de sesion exitoso " + usuario.getNombreCompleto());
                limpiarFormulario();
            } else {
                mostrarError("Contraseña incorrecta.");
            }

        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void irARegistro() {
        System.out.println("Ir a registro");
        main.mostrarRegistro();
    }

    private void mostrarError(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: red;");
        lblMensaje.setText(mensaje);
    }

    private void mostrarExito(String mensaje) {
        lblMensaje.setStyle("-fx-text-fill: green;");
        lblMensaje.setText(mensaje);
    }

    private void limpiarFormulario() {
        txtUsername.clear();
        pfPassword.clear();
    }

    @FXML
    private void cambiarVista() {
        if (main != null) {
            main.mostrarTerminal();
        }
    }
}