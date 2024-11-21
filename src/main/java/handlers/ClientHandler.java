package handlers;

import connections.Server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedReader reader;

    private BufferedWriter writer;

    private String clientUsername;
    private RoomHandler currentRoom;


    public ClientHandler(String username, Socket socket, Server server) {
        this.clientUsername = username;
        this.socket = socket;
        this.server = server;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        } catch (IOException e) {
            close();
        }
    }

    //todo fix if client disconnects, they should disappear from every list

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                if (message.equals("REQUEST REFRESH")) {
                    handleRefreshRequest();
                }
                else if(message.startsWith("CREATED ROOM:")){
                    handleRoomCreation(message);
                }
                else if(message.startsWith("JOIN ROOM:")){
                    handleRoomJoin(message);
                }
                else if (message.startsWith("ROOM MESSAGE:")) {
                    String roomMessage = message.replace("ROOM MESSAGE:", "");
                    sendMessageToCurrentRoom(roomMessage);
                }
                else if(message.startsWith("LEAVE ROOM")){
                    handleRoomLeave();
                }
                else if(message.startsWith("USER_DISCONNECTED")){
                    handleDisconnect();
                }
                else if(message.startsWith("CREATE_PRIVATE_CHAT")){
                    handlePrivateChatCreation(message);
                }

            }
        } catch (IOException e) {
            close();
        }
    }

    private void handleDisconnect() {
        server.getConnectedClients().values().removeIf(value ->(value == this));

    }

    private void handleRoomLeave() {
        currentRoom.removeMember(this);
        currentRoom = null;
    }

    private synchronized void handlePrivateChatCreation(String message) {
        String[] members = message.replace("CREATE_PRIVATE_CHAT", "").split(":");
        String normalizedRoomName = normalizeRoomName(members[0], members[1]);
        System.out.println(normalizedRoomName);
        if (server.getPrivateChats().containsKey(normalizedRoomName)) {
            currentRoom = server.getPrivateChats().get(normalizedRoomName);
            currentRoom.addMember(this);
        } else {
            server.getPrivateChats().put(normalizedRoomName, new RoomHandler(normalizedRoomName));
            currentRoom = server.getPrivateChats().get(normalizedRoomName);
            currentRoom.addMember(this);
        }
    }

    private String normalizeRoomName(String user1, String user2) {
        if (user1.compareTo(user2) < 0) {
            return user1 + ":" + user2;
        } else {
            return user2 + ":" + user1;
        }
    }

    private synchronized void handleRoomJoin(String message) {
        String roomName = message.replace("JOIN ROOM:", "");
        currentRoom = server.getRooms().get(roomName);
        currentRoom.addMember(this);
    }

    public void sendMessageToCurrentRoom(String message) {
        if (currentRoom != null) {
            currentRoom.broadcastMessage(message, this);
        }
    }

    private void handleRoomCreation(String room){
        try{
            String roomName = room.replace("CREATED ROOM:", "");
            if(!server.getRooms().containsKey(roomName)) {
                server.getRooms().put(roomName, new RoomHandler(roomName));
                server.getLobbyList().addRoom(roomName);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleRefreshRequest() {
        Set<String> otherConnectedUsers = server.getOtherConnectedUserNames(this);
        Set<String> rooms = server.getCreatedRooms();

        String response = "CONNECTED_USERS:" + String.join(",", otherConnectedUsers) +
            "\nROOMS:" + String.join(",", rooms);

        sendMessage(response);
    }



    private void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            close();
        }
    }



    public String getClientUsername() {
        return clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    public RoomHandler getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(RoomHandler currentRoom) {
        this.currentRoom = currentRoom;
    }
}