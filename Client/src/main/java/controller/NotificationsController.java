package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import network.Client;
import network.Packet;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NotificationsController implements Initializable {
    private Client client;

    @FXML
    private Button view;

    @FXML
    private Button back;

    @FXML
    private ListView<String> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.client = LoginController.getClient();


        back.setOnAction(new EventHandler<ActionEvent>() { // Goes to admin page
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) back.getScene().getWindow();
                try {
                    switchToScene4(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        view.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("16");

                client.sendMessageToServer(packet);
                listView.getItems().clear();

                try {
                    List<Packet> notifications = client.receiveListFromServer();
                    for(Packet p : notifications) {
                        String s = p.getUsername();
                        listView.getItems().add(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error");
                }
            }
        });
    }


    public void switchToScene4(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../userScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("System");
        stage.show();
    }
}
