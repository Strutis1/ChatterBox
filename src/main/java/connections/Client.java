package connections;

import chat.ChatController;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
            close();
        }
    }

    public void sendRoomMessage(String message) {
        sendMessage("ROOM MESSAGE:" + message);
    }

    private void sendRoomName(String selectedChat) {
            sendMessage("ROOMNAME:" + selectedChat);
    }

    public void close() {
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
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String message = reader.readLine();
                    if (message != null) {
                        Platform.runLater(() -> {
                            if (!message.isEmpty()) {
                                HBox hBoxMessage = new HBox();
                                hBoxMessage.setAlignment(Pos.CENTER_LEFT);
                                hBoxMessage.setPadding(new Insets(5, 10, 5, 5));

                                Text textToReceive = new Text(message);
                                TextFlow textFlowMess = new TextFlow(textToReceive);

                                textFlowMess.setStyle("-fx-font-weight: bold; -fx-background-color: #aaa8a8; -fx-text-fill: white");
                                textFlowMess.setPadding(new Insets(5, 10, 5, 10));
                                textToReceive.setFill(Color.color(0.934, 0.945, 0.966));

                                hBoxMessage.getChildren().add(textFlowMess);
                                chatVBox.getChildren().add(hBoxMessage);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
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
            close();
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

    public void leaveRoom() {
        sendMessage("LEAVE ROOM");
    }
}