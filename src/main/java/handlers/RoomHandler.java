package handlers;


import java.util.HashSet;
import java.util.Set;

public class RoomHandler {

    private String roomName;

    private Set<ClientHandler> participants;

    public RoomHandler(String name) {
        this.roomName = name;
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


    public synchronized void addMember(ClientHandler clientHandler) {
        participants.add(clientHandler);
        broadcastMessage("NOTIFICATION:" + clientHandler.getClientUsername() + " has joined the room.", clientHandler);
    }

    public synchronized void removeMember(ClientHandler clientHandler) {
        participants.remove(clientHandler);
        broadcastMessage("NOTIFICATION:" + clientHandler.getClientUsername() + " has left the room.", clientHandler);
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) {
        if(message.startsWith("NOTIFICATION:")){
            for (ClientHandler participant : participants) {
                 if  (!participant.equals(sender)) {
                    participant.sendMessage(message);
                }
            }
        }else {
            for (ClientHandler participant : participants) {
                if (!participant.equals(sender)) {
                    participant.sendMessage(sender.getClientUsername() + ": " + message);//sender.getClientUsername() + ": " + message
                }
            }
        }
    }


}
