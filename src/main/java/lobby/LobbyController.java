package lobby;

import connections.Client;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyController {

    @FXML
    private Button chatButton;

    @FXML
    private Button connectedListButton;

    @FXML
    private Button createRoomButton;

    @FXML
    private ListView<String> currentLobbyList;

    @FXML
    private Button refreshButton;

    @FXML
    private Button roomListButton;

    @FXML
    private TextField roomName;

    private LobbyList lobbyList;

    private boolean selectedConnected;

    private Client client;


    public void initialize(){

        client = DataHandler.getInstance().getClient();
        client.requestRefresh();

        lobbyList = new LobbyList(currentLobbyList);
        lobbyList.getClientList().setAll(DataHandler.getInstance().getConnectedUsers());
        currentLobbyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        lobbyList.showConnectedList();

        chatButton.setOnAction(this::handleChat);
        connectedListButton.setOnAction(this::showConnected);
        createRoomButton.setOnAction(this::handleRoomCreation);
        refreshButton.setOnAction(this::handleRefresh);
        roomListButton.setOnAction(this::showRooms);


        currentLobbyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (currentLobbyList.getSelectionModel().getSelectedItems().size() == 1) {
                    chatButton.setDisable(false);
                    createRoomButton.setDisable(true);
                }
                else if(currentLobbyList.getSelectionModel().getSelectedItems().size() > 1) {
                    chatButton.setDisable(true);
                    if(!roomName.getText().isEmpty())
                        createRoomButton.setDisable(false);
                }
            }else{
                chatButton.setDisable(true);
                createRoomButton.setDisable(true);
            }
        });
    }


    private void showRooms(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showRoomList();
            selectedConnected = false;
        }
    }

    private void handleRefresh(ActionEvent actionEvent) {
        client.sendMessage("REQUEST REFRESH");
        client.receiveLobbyList();
        updateLobby();
    }

    private void updateLobby() {
        lobbyList.getClientList().setAll(DataHandler.getInstance().getConnectedUsers());
        lobbyList.getRoomList().setAll(DataHandler.getInstance().getCreatedRooms());
    }

    //todo add to handler if we create a room
    private void handleRoomCreation(ActionEvent actionEvent) {
        if(!roomName.getText().isEmpty()){
            client.sendMessage("CREATED ROOM:" + roomName.getText());
        }
    }

    private void showConnected(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showConnectedList();
            selectedConnected = true;
        }
    }

    private void handleChat(ActionEvent actionEvent) {
        try {
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/chat/chat.fxml"));
            Parent chatRoot = chatLoader.load();

            Stage chatStage = new Stage();
            Scene chatScene = new Scene(chatRoot, 389, 578);
            chatStage.setTitle("ChatterBox-lobby");
            chatStage.setScene(chatScene);
            chatStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
