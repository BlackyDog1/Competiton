package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static final int PORT = 6543;
    private Socket socket;

    private ObjectOutputStream outputStream;

    private ObjectInputStream inputStream;

    private String receivedMessage;

    public Client() {
        System.out.println("Say something and the message will be sent to the server: ");
        this.socket = null;

        //For receiving and sending data
        boolean isClose = false;
        try {
            this.socket = new Socket("localhost", PORT);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Error linking client to server");
            closeEverything();
        }
    }

    public void start() {

    }
    public void setReceivedMessage(String message) {
        this.receivedMessage = message;
    }

    public String getReceivedMessage (String message) {
        return this.receivedMessage;
    }

    public void sendMessageToServer (Packet packet) {
        try {
            outputStream.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Packet receieveMessageFromServer () {
        try {
            Packet receivedPacket = (Packet) inputStream.readObject();
            return receivedPacket;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error at receiving message");
            closeEverything();
            return null;
        }
    }

    public List<Packet> receiveListFromServer() {
        try {
            List<Packet> receivedPacket = (List<Packet>) inputStream.readObject();
            return receivedPacket;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeEverything() {
        try {
            this.socket.close();
            this.outputStream.close();
            this.inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Closed connection");
        }

    }

}