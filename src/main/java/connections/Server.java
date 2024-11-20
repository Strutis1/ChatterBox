package connections;

import handlers.ClientHandler;
import handlers.RoomHandler;
import lobby.LobbyList;

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

    //todo can add a listener to rooms and then start room thread after someone creates a room

    private Map<String, ClientHandler> connectedClients = new HashMap<>();
    private Map<String, RoomHandler> rooms = new HashMap<>();

    private LobbyList lobbyList;

    public Server(ServerSocket serverSocket, LobbyList lobbyList) {
        this.serverSocket = serverSocket;
        this.lobbyList = lobbyList;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();

                this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String username = receiveUser();


                ClientHandler clientHandler = new ClientHandler(username, clientSocket, this);
                RoomHandler roomHandler;

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
                lobbyList.addClient(username);
                new Thread(clientHandler).start();


            }
        } catch (IOException e) {
            close();
            e.printStackTrace();
        }
    }

    public String receiveUser(){
        try {
            String str = reader.readLine();
            if(str.startsWith("USERNAME:")){
                return str.replace("USERNAME:", "");
            }
            throw new IOException("cant send username");
        } catch (IOException e) {
            e.printStackTrace();
            close();
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



    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getConnectedUserNames() {
        return connectedClients.keySet();
    }

    public Set<String> getOtherConnectedUserNames(ClientHandler clientHandler){
        Map<String, ClientHandler> temp = new HashMap<>(connectedClients);
        temp.remove(clientHandler.getClientUsername());

        return temp.keySet();
    }

    public Set<String> getCreatedRooms(){
        return rooms.keySet();
    }

    public Map<String, ClientHandler> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(Map<String, ClientHandler> connectedClients) {
        this.connectedClients = connectedClients;
    }

    public Map<String, RoomHandler> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, RoomHandler> rooms) {
        this.rooms = rooms;
    }



    //if i figure out a way to update lobby when a client joins
//    public void broadcastConnectedUsers() {
//        Set<String> connectedUsernames = getConnectedUserNames();
//        Set<String> createdRooms = getCreatedRooms();
//
//        for (Map.Entry<String, ClientHandler> entry : connectedClients.entrySet()) {
//            String clientUsername = entry.getKey();
//            ClientHandler clientHandler = entry.getValue();
//
//            String messageToSend = "CONNECTED_USERS:";
//            for (String username : connectedUsernames) {
//                if (!username.equals(clientUsername)) {
//                    messageToSend += username + ",";
//                }
//            }
//
//            if (messageToSend.endsWith(",")) {
//                messageToSend = messageToSend.substring(0, messageToSend.length() - 1);
//            }
//
//            messageToSend += "\nROOMS:";
//            for (String room : createdRooms) {
//                messageToSend += room + ",";
//            }
//
//            if (messageToSend.endsWith(",")) {
//                messageToSend = messageToSend.substring(0, messageToSend.length() - 1);
//            }
//
//            clientHandler.sendMessage(messageToSend);
//        }
//    }

}
