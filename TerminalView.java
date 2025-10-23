package org.example.sistemalogin;
import org.example.sistemalogin.Models.Usuario;
import org.example.sistemalogin.Models.UsuarioModel;
import org.example.sistemalogin.Utils.PasswordUtils;
import org.example.sistemalogin.Utils.Validator;
import org.example.sistemalogin.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TerminalView {

    private final UsuarioModel usuarioModel;
    private final Scanner scanner;
    private final Main main;

    public TerminalView(Main main) {
        this.usuarioModel = new UsuarioModel();
        this.scanner = new Scanner(System.in);
        this.main = main;
    }

    public void iniciar() {
        System.out.println("Sistema de Logeo - Modo Linux");
        boolean verificador = true;

        while (verificador) {
            System.out.println("1. Registrar usuario");
            System.out.println("2. Iniciar sesion");
            System.out.println("3. Salir");
            int opcion = leerEntero("Elige la operacion: ");
            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    iniciarSesion();
                    break;
                case 3:
                    verificador=false;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private void registrarUsuario() {
        System.out.println("Registra un nuevo usuario");

        String nombreCompleto = leerTexto("Nombre completo: ");
        if (nombreCompleto.trim().isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        LocalDate fechaNacimiento = leerFecha("Fecha de nacimiento");
        if (fechaNacimiento == null) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        String username = leerTexto("Username: ");
        if (username.trim().isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        String correo = leerTexto("Correo electrónico: ");
        if (correo.trim().isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        String password = leerTexto("Contraseña: ");
        if (password.isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        String confirmPassword = leerTexto("Confirmar contraseña: ");
        if (confirmPassword.isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        if (!Validator.isValidEmail(correo)) {
            System.out.println("Correo electrónico inválido.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        if (!PasswordUtils.isPasswordStrong(password)) {
            System.out.println("La contraseña es muy debil");
            return;
        }

        if (!Validator.isAdult(fechaNacimiento)) {
            System.out.println("Debes ser mayor de edad para registrarte.");
            return;
        }

        if (usuarioModel.existeUsuario(username)) {
            System.out.println("El nombre de usuario ya existe.");
            return;
        }

        if (usuarioModel.existeCorreo(correo)) {
            System.out.println("El correo electrónico ya está registrado.");
            return;
        }

        Usuario usuario = new Usuario(
                username.trim(),
                correo.trim().toLowerCase(),
                PasswordUtils.encryptPassword(password),
                nombreCompleto.trim(),
                fechaNacimiento
        );

        if (usuarioModel.insertarUsuario(usuario)) {
            System.out.println("Usuario registrado exitosamente");
        } else {
            System.out.println("Error al registrar el usuario");
        }
    }

    private void iniciarSesion() {
        System.out.println("Inicio de sesion");

        String username = leerTexto("Username: ");
        if (username.trim().isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        String password = leerTexto("Contraseña: ");
        if (password.isEmpty()) {
            System.out.println("Error, el campo esta vacio");
            return;
        }

        if (usuarioModel.verificarCredenciales(username, password)) {
            String nombreCompleto = usuarioModel.obtenerNombreCompleto(username);
            System.out.println("Inicio de sesion exitoso " + nombreCompleto);
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número válido.");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String fechaStr = scanner.nextLine();
                if (fechaStr.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Usa yyyy-mm-dd");
            }
        }
    }
}