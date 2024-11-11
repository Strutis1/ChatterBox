package connections;

import chat.ChatController;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

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
        } catch(IOException e) {
            e.printStackTrace();
            close(localhost, reader, writer);
        }
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

//    public void receiveMessage(VBox chatVBox) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (socket.isConnected()) {
//                    String message = null;
//                    try {
//                        message = reader.readLine();
//                        if (message != null) {
//                            ChatCController.receiveMessage(message, chatVBox);
//                        }
//                    }catch(IOException e) {
//                        e.printStackTrace();
//                        break;
//                    }
//                }
//            }
//        }).start();
//    }


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
}
