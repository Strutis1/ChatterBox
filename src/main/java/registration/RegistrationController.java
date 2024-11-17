package registration;

import handlers.DataHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {

    @FXML
    private TextField clientUsername;

    @FXML
    private Button createButton;

    @FXML
    private Button joinButton;

    @FXML
    private TextField roomName;



    public void initialize() {
        clientUsername.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        roomName.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        roomName.setOnAction(this::checkInput);
        clientUsername.setOnAction(this::checkInput);
        joinButton.setOnAction(this::handleJoin);
        createButton.setOnAction(this::handleCreation);

    }

    private void handleCreation(ActionEvent actionEvent) {

    }

    private void handleJoin(ActionEvent actionEvent) {
        sendInput();
        openChat();
    }

    private void sendInput() {
        try {
            DataHandler.getInstance().setUsername(clientUsername.getText());
            DataHandler.getInstance().setSelectedChat(roomName.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkInput(ActionEvent actionEvent) {
        if(!clientUsername.getText().isEmpty() && !roomName.getText().isEmpty()) {
            joinButton.setDisable(false);
            createButton.setDisable(false);
        }
        else{
            joinButton.setDisable(true);
            createButton.setDisable(true);
        }
    }

    //todo change to chat instead

    private void openChat() {
        try {
            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/chat/chat.fxml"));
            Parent clientRoot = chatLoader.load();

            Stage clientStage = new Stage();
            Scene clientScene = new Scene(clientRoot, 389, 578);
            clientStage.setTitle("ChatterBox");
            clientStage.setScene(clientScene);
            clientStage.show();

            Stage currentStage = (Stage) joinButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}