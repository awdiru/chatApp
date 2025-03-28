package org.avdonin.chatApp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private static final Logger log = LoggerFactory.getLogger(ChatClient.class);

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        log.info("Connected to server.");
    }
}
