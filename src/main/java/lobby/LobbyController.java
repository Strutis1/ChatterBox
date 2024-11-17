package lobby;

import handlers.DataHandler;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

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


    public void initialize(){

        lobbyList = new LobbyList(currentLobbyList);
        lobbyList.getClientList().setAll(DataHandler.getInstance().getConnectedUsers());
        currentLobbyList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



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
                    createRoomButton.setDisable(false);
                }
            }else{
                chatButton.setDisable(true);
                createRoomButton.setDisable(true);
            }
        });

        DataHandler.getInstance().getConnectedUsers().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    lobbyList.getClientList().setAll(DataHandler.getInstance().getConnectedUsers());
                }
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

    }

    private void handleRoomCreation(ActionEvent actionEvent) {
        if(!roomName.getText().isEmpty()){

        }
    }

    private void showConnected(ActionEvent actionEvent) {
        if(lobbyList.getRoomList() != null) {
            lobbyList.showConnectedList();
            selectedConnected = true;
        }
    }

    private void handleChat(ActionEvent actionEvent) {

    }

}