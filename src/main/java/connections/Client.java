package connections;

import chat.ChatController;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static javafx.collections.FXCollections.observableList;

public class Client {
    private String username;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Client(Socket localhost) {
        try {
            this.socket = localhost;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            sendUsername(DataHandler.getInstance().getUsername());
            receiveLobbyList();
        } catch(IOException e) {
            e.printStackTrace();
            close(localhost, reader, writer);
        }
    }

    private void sendRoomName(String selectedChat) {
            sendMessage("ROOMNAME:" + selectedChat);
    }

    public void close(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage(VBox chatVBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    String message = null;
                    try {
                        message = reader.readLine();
                        if (message != null) {
                            ChatController.receiveMessage(message, chatVBox);
                        }
                    }catch(IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();
    }



    public void receiveLobbyList(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String messageOne = null;
                String messageTwo = null;
                try {
                    messageOne = reader.readLine();
                    messageTwo = reader.readLine();

                    if (messageOne.startsWith("CONNECTED_USERS:"))
                        messageOne = messageOne.replace("CONNECTED_USERS:", "");

                    if (messageTwo.startsWith("ROOMS:"))
                        messageTwo = messageTwo.replace("ROOMS:", "");

                    ObservableList<String> connectedUsers = FXCollections.observableArrayList(
                            messageOne != null ? messageOne.split(",") : new String[0]
                    );
                    ObservableList<String> createdRooms = FXCollections.observableArrayList(
                            messageTwo != null ? messageTwo.split(",") : new String[0]
                    );

                    Platform.runLater(() -> {
                        DataHandler.getInstance().setConnectedUsers(connectedUsers);
                        DataHandler.getInstance().setCreatedRooms(createdRooms);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public void sendUsername(String username){
            sendMessage("USERNAME:" + username);
    }



    public void sendMessage(String messageToSend) {
        try {
            writer.write(messageToSend);
            writer.newLine();
            writer.flush();
        }catch(IOException e) {
            e.printStackTrace();
            System.out.println("Cant send message" + messageToSend);
            close(socket, reader, writer);
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public void requestRefresh() {
        sendMessage("REQUEST REFRESH");
    }
}