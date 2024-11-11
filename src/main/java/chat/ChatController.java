package chat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ChatController {

    @FXML
    private VBox chat_VBox;

    @FXML
    private TextField message;

    @FXML
    private ScrollPane scrollPanel;

    @FXML
    private Button sendButton;

    @FXML
    private TextField sender;

}
