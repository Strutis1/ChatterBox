package chat;

import connections.Client;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

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
    private Button exitButton;

    @FXML
    private Label sender;

    private Client client;



    public void initialize(){
        this.client = DataHandler.getInstance().getClient();
        client.receiveMessage(chat_VBox);
        sender.setText(DataHandler.getInstance().getSelectedChat());
        sendButton.setOnAction(this::sendMessageToRoom);
        message.setOnAction(this::sendMessageToRoom);
        exitButton.setOnAction(this::handleExit);

        chat_VBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                scrollPanel.setVvalue((double) newValue);
            }
        });
    }

    private void handleExit(ActionEvent actionEvent) {
        client.leaveRoom();
        Stage currentStage = (Stage) exitButton.getScene().getWindow();
        currentStage.close();
    }

    private void sendMessage(ActionEvent actionEvent) {
        String messageToSend = message.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text textToSend = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(textToSend);

            textFlow.setStyle("-fx-font-weight: bold; -fx-background-color: white;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            textToSend.setFill(Color.color(0.2, 0.4, 0.96));

            hBox.getChildren().add(textFlow);
            chat_VBox.getChildren().add(hBox);

            client.sendMessage(messageToSend);
            message.clear();
        }
    }



    private void sendMessageToRoom(ActionEvent actionEvent) {
        client.sendRoomMessage(message.getText());
    }



}
