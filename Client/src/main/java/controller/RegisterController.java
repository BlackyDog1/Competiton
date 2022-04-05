package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import network.Client;
import network.Packet;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Button backButton; // backButton

    @FXML
    private Button createButton; // createButton

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField roleField;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = LoginController.getClient();

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) backButton.getScene().getWindow();
                try {
                    switchToScene1(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = usernameField.getText();
                String name = nameField.getText();
                String role = roleField.getText();

                if(!name.isEmpty() && !username.isEmpty()) {
                    if(role.equals("Administrator") || role.equals("user")) {
                        Packet packet = new Packet("1");
                        packet.setName(name);
                        packet.setUsername(username);
                        packet.setRole(role);
                        client.sendMessageToServer(packet);

                        try {
                            Packet receivedPacket = client.receieveMessageFromServer();
                            String message = receivedPacket.getIndex();
                            if(message.equals("Register succesful"))
                                System.out.println("Account creation worked!");
                            else
                                System.out.println("Username already taken!");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Failed to receive message");
                        }
                    }
                    else
                        System.out.println("Please type a valid role : Administrator or user");
                }
            }
        });
    }

    public void switchToScene1 (Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../loginScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }
}
