package handlers;

import connections.Server;

import java.util.HashSet;
import java.util.Set;

public class RoomHandler {

    private String roomName;

    private Set<ClientHandler> participants;
    private Server server;

    public RoomHandler(String name, Server server) {
        this.roomName = name;
        this.server = server;
        this.participants = new HashSet<>();
    }

    public Set<ClientHandler> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ClientHandler> participants) {
        this.participants = participants;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }


    public void addMember(ClientHandler clientHandler) {
        participants.add(clientHandler);
        broadcastMessage(clientHandler.getClientUsername() + " has joined the room.", clientHandler);
    }

    public void removeMember(ClientHandler clientHandler) {
        participants.remove(clientHandler);
        broadcastMessage(clientHandler.getClientUsername() + " has left the room.", clientHandler);
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler participant : participants) {
            if (!participant.equals(sender)) {
                participant.sendMessage("ROOM:" + roomName + ":" + sender.getClientUsername() + ": " + message);
            }
        }
    }
}
