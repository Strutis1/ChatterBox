package lobby;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LobbyController {

    @FXML
    private Button chatButton;

    @FXML
    private Button connectedListButton;

    @FXML
    private Button createRoomButton;

    @FXML
    private ListView<?> currentLobbyList;

    @FXML
    private Button refreshButton;

    @FXML
    private Button roomListButton;

    @FXML
    private TextField roomName;

}
