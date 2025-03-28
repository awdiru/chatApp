package org.avdonin.chatApp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private static final Logger log = LoggerFactory.getLogger(ChatServer.class);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        log.info("Server started.");
        Socket clientSocket = serverSocket.accept();
        log.info("Client connected");
    }
}