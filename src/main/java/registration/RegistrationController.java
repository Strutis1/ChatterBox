package registration;

import connections.Client;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Socket;

public class RegistrationController {

    @FXML
    private TextField clientUsername;

    @FXML
    private Button joinButton;

    private Client client;




    public void initialize() {
        clientUsername.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        clientUsername.setOnAction(this::checkInput);
        joinButton.setOnAction(this::handleJoin);

    }

    private void handleJoin(ActionEvent actionEvent) {
        sendInput();

        try {
            client = new Client(new Socket("localhost", 1234));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            DataHandler.getInstance().setClient(client);
            openChat();
        });

    }

    private void sendInput() {
        try {
            DataHandler.getInstance().setUsername(clientUsername.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkInput(ActionEvent actionEvent) {
        if(!clientUsername.getText().isEmpty()) {
            joinButton.setDisable(false);
        }
        else{
            joinButton.setDisable(true);
        }
    }


    private void openChat() {
        try {
            FXMLLoader lobbyLoader = new FXMLLoader(getClass().getResource("/lobby/lobby.fxml"));
            Parent lobbyRoot = lobbyLoader.load();

            Stage lobbyStage = new Stage();
            Scene lobbyScene = new Scene(lobbyRoot, 389, 578);
            lobbyStage.setTitle("ChatterBox-lobby");
            lobbyStage.setResizable(false);
            lobbyStage.setScene(lobbyScene);
            lobbyStage.show();

            Platform.runLater(() -> {
                Stage currentStage = (Stage) joinButton.getScene().getWindow();
                currentStage.close();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}