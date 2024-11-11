package com.crew.mif.chatterbox;

import connections.Server;
import handlers.DataHandler;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerController {

    @FXML
    private VBox chat_VBox;

    @FXML
    private ScrollPane scrollPanel;

    private Server server;


    public void initialize() {
        new Thread(() -> {
            try {
                server = new Server(new ServerSocket(1234));
                server.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

//        DataHandler.getInstance().getConnectedUsers().addListener((ListChangeListener<String>) change -> {
//            while (change.next()) {
//                if (change.wasAdded()) {
//                    for (String username : change.getAddedSubList()) {
//                        displayJoinMessage(username);
//                    }
//                }
//            }
//        });

        chat_VBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                scrollPanel.setVvalue((double) newValue);
            }
        });
    }

    private void displayJoinMessage(String username) {
        HBox hBoxMessage = new HBox();
        hBoxMessage.setAlignment(Pos.CENTER);
        hBoxMessage.setPadding(new Insets(5, 10, 5, 10));
        System.out.println("HELLO");
        Text text = new Text(username + " has joined");
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-font-weight: bold; -fx-background-color: #aaa8a8; -fx-text-fill: white");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.966));

        hBoxMessage.getChildren().add(textFlow);

        Platform.runLater(() -> chat_VBox.getChildren().add(hBoxMessage));
    }

}
