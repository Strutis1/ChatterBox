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
    private RoomHandler currentRoom;


    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        } catch (IOException e) {
            closeConnections();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                if (message.equals("REQUEST REFRESH")) {
                    handleRequestRefresh();
                }
                if(message.startsWith("CREATED ROOM:")){
                    handleRoomCreation(message);
                }
            }
        } catch (IOException e) {
            closeConnections();
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

    private void handleRequestRefresh() {
        try {
            Set<String> connectedUsers = server.getConnectedUserNames();
            Set<String> rooms = server.getCreatedRooms();

            String response = "CONNECTED_USERS:";
            if (!connectedUsers.isEmpty()) {
                for (String user : connectedUsers) {
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

            writer.write(response);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
            closeConnections();
        }
    }



    private void closeConnections() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}