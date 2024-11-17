package handlers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public final class DataHandler {


    private String username;
    private String selectedChat;
    private ObservableList<String> connectedUsers = FXCollections.observableArrayList();

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

    public ObservableList<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void updateConnectedUsers(ObservableList<String> users) {
        connectedUsers.clear();
        connectedUsers.addAll(users);
    }

    public void addConnectedUser(String username) {
        if (!connectedUsers.contains(username)) {
            connectedUsers.add(username);
        }
    }

    public void removeConnectedUser(String username) {
        connectedUsers.remove(username);
    }

    public String getSelectedChat() {
        return selectedChat;
    }

    public void setSelectedChat(String selectedChat) {
        this.selectedChat = selectedChat;
    }
}
