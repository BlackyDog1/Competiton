package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import network.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private Client client;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.client = LoginController.getClient();

        yesButton.setOnAction(new EventHandler<ActionEvent>() { // Goes to admin page
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) yesButton.getScene().getWindow();
                try {
                    switchToScene5(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        noButton.setOnAction(new EventHandler<ActionEvent>() { // Goes to user page
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) noButton.getScene().getWindow();
                try {
                    switchToScene4(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });
    }

    public void switchToScene4(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../userScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User");
        stage.show();
    }

    public void switchToScene5(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../administratorScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Administrator");
        stage.show();
    }

}
