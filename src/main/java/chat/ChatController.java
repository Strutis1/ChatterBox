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
import javafx.stage.WindowEvent;

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
    private Label sender;

    private Client client;



    public void initialize(){
        this.client = DataHandler.getInstance().getClient();
        client.receiveMessage(chat_VBox);
        sender.setText(DataHandler.getInstance().getSelectedChat());
        sendButton.setOnAction(this::sendMessageToRoom);
        message.setOnAction(this::sendMessageToRoom);

        Platform.runLater(() -> {
            Stage currentStage = (Stage) chat_VBox.getScene().getWindow();
            currentStage.setOnCloseRequest(this::handleExit);
        });

        chat_VBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                scrollPanel.setVvalue((double) newValue);
            }
        });
    }

    private void handleExit(WindowEvent windowEvent) {
        client.leaveRoom();
    }

    private void sendMessageToRoom(ActionEvent actionEvent) {
        String messageToSend = message.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text textToSend = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(textToSend);

            textFlow.setStyle("-fx-font-weight: bold; -fx-background-color: darkblue;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            textToSend.setFill(Color.color(1, 1, 1));

            hBox.getChildren().add(textFlow);
            chat_VBox.getChildren().add(hBox);

            client.sendMessage("ROOM MESSAGE:" + messageToSend);
            message.clear();
        }
    }



}
