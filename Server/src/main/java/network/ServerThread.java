package network;

import controller.Controller;
import entity.NotificationsEntity;
import entity.PersonEntity;
import entity.PersonStageEntity;
import entity.TeamEntity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThread extends Thread{
    private Socket socket = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    private Controller controller;

    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            //For receiving and sending data
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.controller = new Controller();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Packet recivePacket = (Packet) this.in.readObject();
                System.out.println("Received: " + recivePacket.getIndex());
                if(recivePacket.getIndex().equals("Closing Application!"))
                {
                    socket.close();
                    in.close();
                    out.close();
                    return;
                }
                execute(recivePacket);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void execute(Packet packet) throws IOException {
        String index = packet.getIndex();
        Packet sendToClient;
        String currentCase;
        switch (index){
            case "1": // register
                currentCase = "1";
                if(controller.registerAccount(packet))
                    sendToClient = new Packet("Register succesful");
                else
                    sendToClient = new Packet("Username already exists!");
                break;
            case "2": // login
                currentCase = "2";
                if(controller.loginAccount(packet)) {
                    if(controller.isAdmin(packet.getUsername()))
                        sendToClient = new Packet("Username is an administrator"); // vedem daca este user sau admin
                    else
                        sendToClient = new Packet("Username is not an administrator");
                }
                else
                    sendToClient = new Packet("There is no person with that username");
                break;
            case "3": // return competitor ranking
                List<PersonEntity> ranking = controller.getPersonRanking();
                List<Packet> sendRanking = new ArrayList<Packet>();
                currentCase = "3";
                for(PersonEntity iterator : ranking)
                {
                    Packet p = new Packet("3");
                    p.setUsername(iterator.getUsername());
                    String score = controller.getScoreByUsername(iterator.getUsername());
                    p.setName(score);
                    sendRanking.add(p);
                }
                try {
                    this.out.writeObject(sendRanking);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendToClient = new Packet("Couldn't send ranking");
                break;
            case "4": // return team ranking
                List<TeamEntity> teamRanking = controller.getTeamRanking();
                List<Packet> sendTeam = new ArrayList<Packet>();
                currentCase = "4";
                for(TeamEntity iterator : teamRanking)
                {
                    Packet p = new Packet("4");
                    p.setUsername(iterator.getTeamName());
                    String score = controller.getScoreByTeamName(iterator.getTeamName());
                    p.setName(score);
                    sendTeam.add(p);
                }

                try {
                    this.out.writeObject(sendTeam);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendToClient = new Packet("Couldn't send ranking");
                break;
            case "5": // register score for received stage name
                currentCase = "5";
                String stageName = packet.getUsername();
                String score = packet.getName();
                if(controller.setScore(stageName, score)) {
                    sendToClient = new Packet("Done");
                    controller.deleteNotification(stageName);
                }
                else
                    sendToClient = new Packet("Stage does not exist");
                break;
            case "6": // add competitor to team
                currentCase = "6";

                String username = packet.getUsername();
                String teamName = packet.getName();
                if(controller.addCompetitorToTeam(username, teamName))
                    sendToClient = new Packet("Done");
                else
                    sendToClient = new Packet ("Team already has 5 members! or team/username does not exist");
                break;
            case "7": // enroll competitor in stage
                currentCase = "7";

                String userName = packet.getUsername();
                String stage = packet.getName();

                if(controller.enrollCompetitor(userName, stage)) {
                    controller.sendNotification(userName, stage);
                    sendToClient = new Packet("Done");
                }
                else
                    sendToClient = new Packet("Username already enrolled in this stage or stage/username does not exist");
                break;
            case "8": // create team
                currentCase = "8";

                String newTeam = packet.getUsername();
                controller.createTeam(newTeam);
                sendToClient = new Packet("Created team");
                break;
            case "9": // create stage
                currentCase = "9";

                String newStage = packet.getUsername();
                controller.createStage(newStage);
                sendToClient = new Packet("Created stage");
                break;
            case "10": // delete team
                currentCase = "10";

                String deletedTeam = packet.getUsername();
                if(controller.deleteTeam(deletedTeam))
                    sendToClient = new Packet("Deleted team!");
                else
                    sendToClient = new Packet("Team does not exist!");
                break;
            case "11": // delete user/admin
                currentCase = "11";

                String deletedUser = packet.getUsername();
                if(controller.deleteUser(deletedUser))
                    sendToClient = new Packet("Deleted user!");
                else
                    sendToClient = new Packet("Username does not exist!");
                break;
            case "12": // delete stage
                currentCase = "12";

                String deleteStage = packet.getUsername();
                if(controller.deleteStage(deleteStage))
                    sendToClient = new Packet("Deleted stage!");
                else
                    sendToClient = new Packet("Stage does not exist!");
                break;
            case "13": // show scoreBoard
                currentCase = "13";
                List<PersonStageEntity> unverifiedPersons = controller.getAllUnverifiedPersons();
                List<Packet> scoreBoard= new ArrayList<Packet>();
                for(PersonStageEntity ps : unverifiedPersons) {
                    String usernameById = controller.getUsernameById(ps.getIdPerson());
                    String stageNameById = controller.getStageNameById(ps.getIdStage());

                    Packet packet1 = new Packet("13");
                    packet1.setUsername(usernameById);
                    packet1.setName(stageNameById);
                    packet1.setRole(String.valueOf(ps.getScore()));

                    scoreBoard.add(packet1);
                }
                try {
                    this.out.writeObject(scoreBoard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendToClient = new Packet("Couldn't send scoreboard");
                break;
            case "14": // register selected score
                currentCase = "14";

                String username1 = packet.getUsername();
                String stage1 = packet.getName();

                if(controller.verifyCompetitor(username1, stage1))
                    sendToClient = new Packet("Registered the score!");
                else
                    sendToClient = new Packet("Failed to register the score!");
                break;
            case "15": // show ranking by stage
                currentCase = "15";

                String stageRank = packet.getUsername();

                List<PersonEntity> personEntities = controller.getCompetitorsByStage(stageRank);
                List<Packet> stageScoreBoard= new ArrayList<Packet>();
                if(personEntities != null) {
                    for (PersonEntity personEntity : personEntities) {
                        Packet packet1 = new Packet("15");
                        packet1.setUsername(personEntity.getUsername());
                        packet1.setName(controller.getScoreByUsernameAndStage(personEntity.getUsername(), stageRank));

                        stageScoreBoard.add(packet1);
                    }
                    try {
                        this.out.writeObject(stageScoreBoard);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Packet sendPacket = new Packet("There is no stage with that name!");
                    stageScoreBoard.add(sendPacket);
                    this.out.writeObject(stageScoreBoard);

                }
                sendToClient = new Packet("Couldn't Show ranking by stage");
                break;
            case "16": // view notificatins
                currentCase = "16";

                List<NotificationsEntity> notifications = controller.getCurrentUserNotifications();
                List<Packet> sendNotif = new ArrayList<Packet>();
                if(notifications != null) {
                    for (NotificationsEntity notif : notifications) {
                        Packet packet1 = new Packet("16");
                        packet1.setUsername(controller.getStageNameById(notif.getIdStage()));

                        sendNotif.add(packet1);
                    }

                    try {
                        this.out.writeObject(sendNotif);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Packet sendPacket = new Packet("There are no notifications!");
                    sendNotif.add(sendPacket);
                    this.out.writeObject(sendNotif);
                }
                sendToClient = new Packet("Couldn't show notifications!");
                break;
            case "17": // send notification to competitor
                currentCase = "17";

                String username2 = packet.getUsername();
                String stage2 = packet.getName();

                controller.sendNotification(username2, stage2);
                sendToClient = new Packet("Sent notification!");

                break;
            default:
                currentCase = "default";
                sendToClient = new Packet("Can't understand you :/");
        }
        if(!currentCase.equals("3") && !currentCase.equals("4") && !currentCase.equals("13") && !currentCase.equals("15") && !currentCase.equals("16")) // Exception when server receives lists
            try {
                this.out.writeObject(sendToClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}