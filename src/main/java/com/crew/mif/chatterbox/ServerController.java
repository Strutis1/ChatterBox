package com.crew.mif.chatterbox;

import connections.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import lobby.LobbyList;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerController {

    @FXML
    private Button connectedListButton;

    @FXML
    private ListView<String> currentLobbyList;

    @FXML
    private Button refreshButton;

    @FXML
    private Button roomListButton;

    private Server server;

    private LobbyList lobbyList;

    private boolean selectedConnected;


    public void initialize() {
        lobbyList = new LobbyList(currentLobbyList);
        new Thread(() -> {
            try {
                server = new Server(new ServerSocket(1234));
                server.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        connectedListButton.setOnAction(this::showConnected);
        refreshButton.setOnAction(this::handleRefresh);
        roomListButton.setOnAction(this::showRooms);

    }

    private void showRooms(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showRoomList();
            selectedConnected = false;
        }
    }

    private void handleRefresh(ActionEvent actionEvent) {
            lobbyList.getClientList().clear();
            lobbyList.getClientList().setAll(server.getConnectedUserNames());
            lobbyList.getRoomList().setAll(server.getCreatedRooms());
    }

    private void showConnected(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showConnectedList();
            selectedConnected = true;
        }
    }

}