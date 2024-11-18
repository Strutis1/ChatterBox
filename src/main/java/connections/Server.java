package connections;

import handlers.ClientHandler;
import handlers.RoomHandler;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Map<String, ClientHandler> connectedClients = new HashMap<>();
    private Map<String, RoomHandler> rooms = new HashMap<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();

                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                RoomHandler roomHandler;

                String username = receiveUser();
//                String roomName = receiveRoomName();

//                if(!rooms.containsKey(roomName)) {
//                    roomHandler = new RoomHandler(roomName, this);
//                    roomHandler.addMember(clientHandler);
//                    rooms.put(roomName, roomHandler);
//                } else{
//                    roomHandler = rooms.get(roomName);
//                    roomHandler.addMember(clientHandler);
//                }


                connectedClients.put(username, clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveUser(){
        try {
            String str = reader.readLine();
            System.out.println("Received from client: " + str);
            if(str.startsWith("USERNAME:")){
                return str.replace("USERNAME:", "");
            }
            throw new IOException("cant send username");
        } catch (IOException e) {
            e.printStackTrace();
            close(serverSocket, reader, writer);
            return "";
        }
    }

//    public String receiveRoomName(){
//        try {
//            String str = reader.readLine();
//            System.out.println("Received from client: " + str);
//            if(str.startsWith("ROOMNAME:")){
//                return str.replace("ROOMNAME:", "");
//            }
//            throw new IOException("cant send room name");
//        } catch (IOException e) {
//            e.printStackTrace();
//            close(serverSocket, reader, writer);
//            return "";
//        }
//    }


    public void close(ServerSocket socket, BufferedReader reader, BufferedWriter writer) {
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

    public Set<String> getConnectedUserNames() {
        return connectedClients.keySet();
    }

    public Set<String> getCreatedRooms(){
        return rooms.keySet();
    }
}