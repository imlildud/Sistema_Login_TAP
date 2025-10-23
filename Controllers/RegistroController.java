package org.example.sistemalogin.Controllers;
import org.example.sistemalogin.Main;
import org.example.sistemalogin.Models.Usuario;
import org.example.sistemalogin.Models.UsuarioModel;
import org.example.sistemalogin.Utils.PasswordUtils;
import org.example.sistemalogin.Utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistroController {
    @FXML private TextField txtNombreCompleto;
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private TextField txtUsername;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField pfPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private Label lblMensaje;
    @FXML private Button btnRegistrar;
    @FXML private Button btnIniciarSesion;
    @FXML private Button btnCambiarVista;

    private final UsuarioModel usuarioModel;

    public RegistroController() {
        this.usuarioModel = new UsuarioModel();
    }

    private Main main;
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        lblMensaje.setText("");

        btnRegistrar.setOnAction(e -> registrarUsuario());
        btnIniciarSesion.setOnAction(e -> irAInicioSesion());
        btnCambiarVista.setOnAction(e -> cambiarVista());
    }

    @FXML
    private void registrarUsuario() {
        try {
            if (!validarCamposVacios()) {
                return;
            }
            if (!Validator.isValidEmail(txtCorreo.getText())) {
                mostrarError("Correo electronico no es valido.");
                return;
            }
            if (!pfPassword.getText().equals(pfConfirmPassword.getText())) {
                mostrarError("Las contraseñas no coinciden.");
                return;
            }
            if (!PasswordUtils.isPasswordStrong(pfPassword.getText())) {
                mostrarError("La contraseña es debil");
                return;
            }
            if (!Validator.isAdult(dpFechaNacimiento.getValue())) {
                mostrarError("Debes ser mayor de edad para registrarte.");
                return;
            }
            if (usuarioModel.existeUsuario(txtUsername.getText())) {
                mostrarError("El nombre de usuario ya existe.");
                return;
            }
            if (usuarioModel.existeCorreo(txtCorreo.getText())) {
                mostrarError("El correo electronico ya está registrado.");
                return;
            }

            Usuario usuario = new Usuario(
                    txtUsername.getText().trim(),
                    txtCorreo.getText().trim(),
                    PasswordUtils.encryptPassword(pfPassword.getText()),
                    txtNombreCompleto.getText().trim(),
                    dpFechaNacimiento.getValue()
            );

            if (usuarioModel.insertarUsuario(usuario)) {
                mostrarExito("Usuario registrado exitosamente");
                limpiarFormulario();
            } else {
                mostrarError("Error al registrar el usuario");
            }
        } catch (Exception e) {
            mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void irAInicioSesion() {
        System.out.println("Ir a inicio de sesion");
        main.mostrarLogin();
    }

    @FXML
    private void cambiarVista() {
        if (main != null) {
            main.mostrarTerminal();
        }
    }

    private boolean validarCamposVacios() {
        if (txtNombreCompleto.getText() == null || txtNombreCompleto.getText().trim().isEmpty()) {
            mostrarError("El nombre completo es obligatorio.");
            return false;
        }
        if (dpFechaNacimiento.getValue() == null) {
            mostrarError("La fecha de nacimiento es obligatoria.");
            return false;
        }
        if (txtUsername.getText() == null || txtUsername.getText().trim().isEmpty()) {
            mostrarError("El nombre de usuario es obligatorio.");
            return false;
        }
        if (txtCorreo.getText() == null || txtCorreo.getText().trim().isEmpty()) {
            mostrarError("El correo electronico es obligatorio.");
            return false;
        }
        if (pfPassword.getText() == null || pfPassword.getText().isEmpty()) {
            mostrarError("La contraseña es obligatoria.");
            return false;
        }
        if (pfConfirmPassword.getText() == null || pfConfirmPassword.getText().isEmpty()) {
            mostrarError("Confirma la contraseña.");
            return false;
        }
        return true;
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
        txtNombreCompleto.clear();
        dpFechaNacimiento.setValue(null);
        txtUsername.clear();
        txtCorreo.clear();
        pfPassword.clear();
        pfConfirmPassword.clear();
    }
}