package org.example.sistemalogin;
import javafx.application.Platform;
import org.example.sistemalogin.Controllers.LoginController;
import org.example.sistemalogin.Controllers.RegistroController;
import org.example.sistemalogin.TerminalView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Sistema de Login");

            mostrarLogin();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error al iniciar: " + e.getMessage());
        }
    }

    public void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sistemalogin/login-view.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setMain(this);

            Scene scene = new Scene(root, 400, 350);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error cargando login: " + e.getMessage());
        }
    }

    public void mostrarRegistro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/sistemalogin/registro-view.fxml"));
            Parent root = loader.load();

            RegistroController controller = loader.getController();
            controller.setMain(this);

            Scene scene = new Scene(root, 500, 500);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaError("Error cargando registro: " + e.getMessage());
        }
    }

    public void mostrarTerminal() {
        try {
            if (primaryStage != null) {
                primaryStage.close();
            }

            System.out.println("Cambiando a interfaz de terminal...");
            TerminalView terminalView = new TerminalView(this);
            terminalView.iniciar();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error iniciando terminal: " + e.getMessage());
        }
    }

    public void volverAGrafica() {
        try {
            Platform.runLater(() -> {
                mostrarLogin();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlertaError(String mensaje) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        if (args.length > 0 && "terminal".equals(args[0])) {
            TerminalView terminalView = new TerminalView(null);
            terminalView.iniciar();
        } else {
            launch(args);
        }
    }
}