package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;
import network.Packet;
import javafx.scene.Parent;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private TextField textField;

    private static Client client;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String typedUsername = textField.getText();
                System.out.println(typedUsername);
                if(!typedUsername.isEmpty()) {
                    System.out.println("merge");
                    Packet p = new Packet("2");
                    p.setUsername(textField.getText());
                    client.sendMessageToServer(p);

                    try {
                        Packet receivedPacket = client.receieveMessageFromServer();
                        System.out.println(receivedPacket.getIndex());
                        if(receivedPacket.getIndex().equals("Username is an administrator")){ //if the user is an admin
                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            try {
                                switchToScene3(stage);
                            } catch (Exception e) {
                                System.out.println("Couldn't change the scene!");
                                e.printStackTrace();
                            }
                        }
                        else
                            if(receivedPacket.getIndex().equals("Username is not an administrator")) { //if the user is not an admin
                                Stage stage = (Stage) loginButton.getScene().getWindow();
                                try {
                                    switchToScene4(stage);
                                } catch (Exception e) {
                                    System.out.println("Couldn't change the scene!");
                                    e.printStackTrace();
                                }
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed to receive message");
                    }
                }

            }
        });

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) registerButton.getScene().getWindow();
                try {
                    switchToScene2(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });
    }
    public void switchToScene2(Stage stage) throws IOException {  //REGISTER
        Parent root = FXMLLoader.load(getClass().getResource("../registerScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }

    public void switchToScene3(Stage stage) throws IOException { //Intermediate stage
        Parent root = FXMLLoader.load(getClass().getResource("../adminScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("System");
        stage.show();
    }

    public void switchToScene4(Stage stage) throws IOException { //Main stage for users
        Parent root = FXMLLoader.load(getClass().getResource("../userScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("User");
        stage.show();
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client client) {
        LoginController.client = client;
    }
}
