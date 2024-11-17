package handlers;

import connections.Server;

import java.io.*;
import java.net.Socket;
import java.util.Set;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientName;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        } catch (IOException e) {
            closeConnections();
        }
    }

    @Override
    public void run() {

    }

    private void closeConnections() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            closeConnections();
        }
    }
}