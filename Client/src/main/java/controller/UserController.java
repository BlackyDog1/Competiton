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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Client;
import network.Packet;
import utility.Ranking;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    private Client client;

    @FXML
    private Button logout;

    @FXML
    private Button teamRanking;

    @FXML
    private Button competitorRanking;

    @FXML
    private Button sendScore;

    @FXML
    private Button viewNotifications;

    @FXML
    private TextField textField;

    @FXML
    private ListView<Ranking> listView;

    @FXML
    private TextField scoreTextField;

    @FXML
    private Button scoreByStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.client = LoginController.getClient();

        logout.setOnAction(new EventHandler<ActionEvent>() { // Goes to admin page
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) logout.getScene().getWindow();
                try {
                    switchToScene1(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        competitorRanking.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("3");
                client.sendMessageToServer(packet);
                listView.getItems().clear();
                try{
                    List<Packet> receivedPacket = client.receiveListFromServer();
                    for(Packet p : receivedPacket) {
                        Ranking ranking = new Ranking();
                        ranking.setName(p.getUsername());
                        ranking.setScore(p.getName());
                        listView.getItems().add(ranking);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to receieve ranking!");
                }
            }
        });

        teamRanking.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("4");
                client.sendMessageToServer(packet);
                listView.getItems().clear();
                try {
                    List<Packet> receivedPacket = client.receiveListFromServer();
                    for(Packet p :receivedPacket) {
                        Ranking ranking = new Ranking();
                        ranking.setName(p.getUsername());
                        ranking.setScore(p.getName());
                        listView.getItems().add(ranking);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to receive ranking!");
                }
            }
        });

        sendScore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = textField.getText();
                String score = scoreTextField.getText();
                if (!username.isEmpty() && !score.isEmpty()) {
                    Packet packet = new Packet("5");
                    packet.setUsername(textField.getText());
                    packet.setName(scoreTextField.getText());
                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't send score");
                    }

                }
            }
        });

        viewNotifications.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) viewNotifications.getScene().getWindow();
                try {
                    switchToScene6(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        scoreByStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String stageName = textField.getText();
                if(!stageName.isEmpty()) {
                    Packet packet = new Packet("15");
                    packet.setUsername(stageName);
                    client.sendMessageToServer(packet);
                    listView.getItems().clear();
                    try {
                        List<Packet> receivedPacket = client.receiveListFromServer();
                        for(Packet p :receivedPacket) {
                            Ranking ranking = new Ranking();
                            ranking.setName(p.getUsername());
                            ranking.setScore(p.getName());
                            listView.getItems().add(ranking);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Failed to receive ranking!");
                    }
                }
            }
        });
    }

    public void switchToScene6(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../notificationsScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("System");
        stage.show();
    }

    public void switchToScene1(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../loginScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("System");
        stage.show();
    }

}
