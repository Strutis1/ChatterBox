package lobby;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;


public class LobbyList {
    private ListView<String> currentShowingList;
    private ObservableList<String> clientList = FXCollections.observableArrayList();
    private ObservableList<String> roomList = FXCollections.observableArrayList();


    public LobbyList(ListView<String> lobbyList){
        this.currentShowingList = lobbyList;
        showConnectedList();
    }

    public void showConnectedList(){
        currentShowingList.setItems(clientList);
    }

    public void showRoomList(){
        currentShowingList.setItems(roomList);
    }

    public ObservableList<String> getClientList() {
        return clientList;
    }


    public void setClientList(ObservableList<String> clientList) {
        this.clientList = clientList;
    }

    public ObservableList<String> getRoomList() {
        return roomList;
    }

    public void setRoomList(ObservableList<String> roomList) {
        this.roomList = roomList;
    }

    public void setVisible(boolean visible){
        currentShowingList.setVisible(visible);
    }

    public void addClient(String client) {
        clientList.add(client);
    }

    public void addRoom(String roomName) {
        roomList.add(roomName);
    }
}