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
    private Button registerButton;


    public void initialize() {
        clientUsername.setStyle("-fx-background-color: darkblue; -fx-text-fill: white;");
        registerButton.setOnAction(this::checkUsername);
    }

    private boolean sendUsername() {
        try {
            DataHandler handler = DataHandler.getInstance();
            handler.setUsername(clientUsername.getText());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkUsername(ActionEvent actionEvent) {
        if(!clientUsername.getText().isEmpty()){
            if (sendUsername()) {
                openLobby();
            } else {
                System.out.println("Username could not be sent. Registration failed.");
            }
        } else {
            System.out.println("Please enter a valid username.");
        }
    }

    private void openLobby() {
        try {
            FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("/lobby/lobbyC.fxml"));
            Parent clientRoot = clientLoader.load();

            Stage clientStage = new Stage();
            Scene clientScene = new Scene(clientRoot, 389, 578);
            clientStage.setTitle("ChatterBox-lobby");
            clientStage.setScene(clientScene);
            clientStage.show();

            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
