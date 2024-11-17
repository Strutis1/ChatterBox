package handlers;

import connections.Server;

import java.net.Socket;

public class RoomHandler {
    Socket socket;
    Server server;

    public RoomHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
}
