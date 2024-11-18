package chat;

import connections.Client;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
        if (!message.isEmpty()) {
            HBox hBoxMessage = new HBox();
            hBoxMessage.setAlignment(Pos.CENTER_LEFT);
            hBoxMessage.setPadding(new Insets(5, 10, 5, 5));

            Text textToReceive = new Text(message);
            TextFlow textFlowMess = new TextFlow(textToReceive);

            textFlowMess.setStyle("-fx-font-weight: bold; -fx-background-color: #aaa8a8; -fx-text-fill: white");
            textFlowMess.setPadding(new Insets(5, 10, 5, 10));
            textToReceive.setFill(Color.color(0.934, 0.945, 0.966));

            hBoxMessage.getChildren().add(textFlowMess);

            Platform.runLater(() -> chatVBox.getChildren().add(hBoxMessage));
        }
    }


    public void initialize(){
        this.client = DataHandler.getInstance().getClient();
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

    }



}
