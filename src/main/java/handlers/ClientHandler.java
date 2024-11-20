package handlers;

import connections.Server;

import java.io.*;
import java.net.Socket;
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

    private void handleRoomJoin(String message) {
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
            server.getRooms().put(roomName, new RoomHandler(roomName, server));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleRefreshRequest() {
            Set<String> otherConnectedUsers = server.getOtherConnectedUserNames(this);
            Set<String> rooms = server.getCreatedRooms();

            String response = "CONNECTED_USERS:";
            if (!otherConnectedUsers.isEmpty()) {
                for (String user : otherConnectedUsers) {
                    response += user + ",";
                }
                response = response.substring(0, response.length() - 1);
            }
            response += "\nROOMS:";
            if (!rooms.isEmpty()) {
                for (String room : rooms) {
                    response += room + ",";
                }
                response = response.substring(0, response.length() - 1);
            }

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