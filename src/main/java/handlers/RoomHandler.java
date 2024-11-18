package handlers;

import connections.Server;

import java.util.Set;

public class RoomHandler implements Runnable {

    private String roomName;
    private Set<ClientHandler> participants;
    private Server server;

    public RoomHandler(String name, Server server) {
        this.roomName = name;
        this.server = server;
    }

    public void addMember(ClientHandler clientHandler){
        participants.add(clientHandler);
    }


    @Override
    public void run() {

    }
}
