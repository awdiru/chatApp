package org.avdonin.chatApp.server;

import org.avdonin.chatApp.server.handler.ClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        log.info("Server started.");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            log.info("Client connected: " + clientSocket);

            ClientHandler clientTread = new ClientHandler(clientSocket, clients);
            clients.add(clientTread);
            new Thread(clientTread).start();
        }
    }
}