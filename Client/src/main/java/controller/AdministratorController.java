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
import utility.ScoreboardValues;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdministratorController implements Initializable {
    private Client client;

    @FXML
    private TextField teamTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private TextField stageTextField;

    @FXML
    private Button addUserToTeam;

    @FXML
    private Button addUserToStage;

    @FXML
    private Button createTeam;

    @FXML
    private Button createStage;

    @FXML
    private Button deleteTeam;

    @FXML
    private Button deleteUser;

    @FXML
    private Button deleteStage;

    @FXML
    private Button confirmScore;

    @FXML
    private Button sendNotification;

    @FXML
    private Button viewRankings;

    @FXML
    private Button refresh;

    @FXML
    private ListView<ScoreboardValues> listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.client = LoginController.getClient();

        viewRankings.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) viewRankings.getScene().getWindow();
                try {
                    switchToScene4(stage);
                } catch (Exception e) {
                    System.out.println("Couldn't change the scene!");
                    e.printStackTrace();
                }
            }
        });

        addUserToTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = userTextField.getText();
                String teamName = teamTextField.getText();
                if(!username.isEmpty() && !teamName.isEmpty()){
                    Packet packet = new Packet("6");
                    packet.setUsername(username);
                    packet.setName(teamName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't enroll user in team");
                    }
                }
            };
        });

        addUserToStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = userTextField.getText();
                String stageName = stageTextField.getText();
                if(!username.isEmpty() && !stageName.isEmpty()) {
                    Packet packet = new Packet("7");
                    packet.setUsername(username);
                    packet.setName(stageName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't enroll user in stage");
                    }
                }
            }
        });

        createTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String teamName = teamTextField.getText();
                if(!teamName.isEmpty())
                {
                    Packet packet = new Packet("8");
                    packet.setUsername(teamName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't create team!");
                    }
                }
            }
        });

        createStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String stageName = stageTextField.getText();
                if(!stageName.isEmpty()) {
                    Packet packet = new Packet("9");
                    packet.setUsername(stageName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't create stage!");
                    }
                }
            }
        });

        deleteTeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String teamName = teamTextField.getText();
                if(!teamName.isEmpty()) {
                    Packet packet = new Packet("10");
                    packet.setUsername(teamName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't delete team!");
                    }
                }
            }
        });

        deleteUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = userTextField.getText();
                if(!username.isEmpty()) {
                    Packet packet = new Packet("11");
                    packet.setUsername(username);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't delete user!");
                    }
                }
            }
        });

        deleteStage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String stageName = stageTextField.getText();
                if(!stageName.isEmpty()) {
                    Packet packet = new Packet("12");
                    packet.setUsername(stageName);

                    client.sendMessageToServer(packet);

                    try {
                        Packet packet1 = client.receieveMessageFromServer();
                        System.out.println(packet1.getIndex());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Couldn't delete stage!");
                    }
                }
            }
        });

        refresh.setOnAction(new EventHandler<ActionEvent>() { // arata toti competitorii care nu au scorul verificat
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("13");

                client.sendMessageToServer(packet);
                listView.getItems().clear();
                try {
                    List<Packet> scoreboard = client.receiveListFromServer();
                    for(Packet p : scoreboard) {
                        ScoreboardValues scoreboardValues = new ScoreboardValues();
                        scoreboardValues.setScore(p.getRole());
                        scoreboardValues.setUsername(p.getUsername());
                        scoreboardValues.setStage(p.getName());
                        listView.getItems().add(scoreboardValues);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error");
                }
            }
        });

        confirmScore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("14");

                ScoreboardValues scoreboardValues = listView.getSelectionModel().getSelectedItem();
                packet.setUsername(scoreboardValues.getUsername());
                packet.setName(scoreboardValues.getStage());

                client.sendMessageToServer(packet);

                try {
                    Packet packet1 = client.receieveMessageFromServer();
                    System.out.println(packet1.getIndex());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Couldn't register the score!");
                }
            }
        });

        sendNotification.setOnAction(new EventHandler<ActionEvent>() { // send notification
            @Override
            public void handle(ActionEvent event) {
                Packet packet = new Packet("17");

                ScoreboardValues scoreboardValues = listView.getSelectionModel().getSelectedItem();
                packet.setUsername(scoreboardValues.getUsername());
                packet.setName(scoreboardValues.getStage());

                client.sendMessageToServer(packet);

                try {
                    Packet packet1 = client.receieveMessageFromServer();
                    System.out.println(packet1.getIndex());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Couldn't register the score!");
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
