package handlers;


import java.util.ArrayList;
import java.util.List;

public final class DataHandler {


    private String username;
    private String selectedChat;
    private List<String> connectedUsers = new ArrayList<>();

    private final static DataHandler INSTANCE = new DataHandler();

    private DataHandler() {}

    public static DataHandler getInstance() {
        return INSTANCE;
    }

    public void setUsername(String text) {
        this.username = text;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void updateConnectedUsers(List<String> users) {
        connectedUsers.clear();
        connectedUsers.addAll(users);
    }
}
