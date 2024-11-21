package lobby;

import connections.Client;
import handlers.DataHandler;
import handlers.RoomHandler;
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
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

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

    private boolean selectedConnected = true;

    private Client client;


    public void initialize(){

        client = DataHandler.getInstance().getClient();
        client.requestRefresh();

        lobbyList = new LobbyList(currentLobbyList);
        updateLobby();

        lobbyList.showConnectedList();

        chatButton.setOnAction(this::handleChat);
        connectedListButton.setOnAction(this::showConnected);
        createRoomButton.setOnAction(this::handleRoomCreation);
        refreshButton.setOnAction(this::handleRefresh);
        roomListButton.setOnAction(this::showRooms);
        roomName.setOnAction(this::handleRoomName);
        Platform.runLater(() -> {
            Stage currentStage = (Stage) chatButton.getScene().getWindow();
            currentStage.setOnCloseRequest(this::handleExit);
        });


        currentLobbyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (currentLobbyList.getSelectionModel().getSelectedItems().size() == 1) {
                    chatButton.setDisable(false);
                }
            }else{
                chatButton.setDisable(true);
            }
        });
    }

    private void handleExit(WindowEvent windowEvent) {
        client.sendMessage("USER_DISCONNECTED");
        Platform.runLater(() ->{client.close();});
    }


    private void handleRoomName(ActionEvent actionEvent) {
        if(!roomName.getText().isEmpty()) {
            createRoomButton.setDisable(false);
        } else{
            createRoomButton.setDisable(true);
        }
    }


    private void showRooms(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showRoomList();
            selectedConnected = false;
            roomName.setVisible(true);
            createRoomButton.setVisible(true);
        }
    }

    private void handleRefresh(ActionEvent actionEvent) {
        client.sendMessage("REQUEST REFRESH");
        Platform.runLater(() ->{
            client.receiveLobbyList();
            updateLobby();
        });
    }

    private void updateLobby() {
        lobbyList.getClientList().setAll(DataHandler.getInstance().getConnectedUsers());
        lobbyList.getRoomList().setAll(DataHandler.getInstance().getCreatedRooms());
    }

    private void handleRoomCreation(ActionEvent actionEvent) {
        if(!roomName.getText().isEmpty()){
            client.sendMessage("CREATED ROOM:" + roomName.getText());
            roomName.clear();
            createRoomButton.setDisable(true);
            Platform.runLater(this::updateLobby);
        }
    }

    private void showConnected(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showConnectedList();
            selectedConnected = true;
            roomName.setVisible(true);
            createRoomButton.setVisible(true);
        }
    }

    private void handleChat(ActionEvent actionEvent) {
        try {
            if(selectedConnected){
                DataHandler.getInstance().setSelectedChat(currentLobbyList.getSelectionModel().getSelectedItem());
                client.sendMessage("CREATE_PRIVATE_CHAT" + client.getUsername() + ":" + currentLobbyList.getSelectionModel().getSelectedItem());
            } else{
                String selectedRoom = currentLobbyList.getSelectionModel().getSelectedItem();
                client.sendMessage("JOIN ROOM:" + selectedRoom);
                DataHandler.getInstance().setSelectedChat(selectedRoom);
            }
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/chat/chat.fxml"));
            Parent chatRoot = chatLoader.load();

            Stage chatStage = new Stage();
            Scene chatScene = new Scene(chatRoot, 389, 578);
            chatStage.setTitle("ChatterBox");
            chatStage.setAlwaysOnTop(true);
            chatStage.setResizable(false);
            chatStage.setScene(chatScene);
            chatStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
