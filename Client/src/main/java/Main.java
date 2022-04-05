import controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import network.Packet;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginScene.fxml"));
        Parent root=(Parent) fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
        Client client = new Client();
        LoginController.setClient(client);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Client client = LoginController.getClient();
                Packet exit = new Packet("Closing Application!");
                client.sendMessageToServer(exit);
                Platform.exit();
                System.exit(0);
            }
        });
    }
}