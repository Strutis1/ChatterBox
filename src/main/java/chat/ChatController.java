package chat;

import connections.Client;
import handlers.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.Socket;

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

    private Client client;

    public static void receiveMessage(String message, VBox chatVBox) {

    }


    public void initialize(){

        try {
            client = new Client(new Socket("localhost", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sender.setStyle("-fx-text-fill: darkblue; -fx-background-color: cornflowerblue;");
        sender.setText(DataHandler.getInstance().getSelectedChat());
        sendButton.setOnAction(this::sendMessageToRoom);
        message.setOnAction(this::sendMessageToRoom);

        chat_VBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                scrollPanel.setVvalue((double) newValue);
            }
        });
    }

    private void sendMessageToRoom(ActionEvent actionEvent) {

    }



}
