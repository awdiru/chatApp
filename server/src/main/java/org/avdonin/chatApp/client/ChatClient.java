package org.avdonin.chatApp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.*;
import java.util.*;
import java.io.IOException;
import java.net.Socket;

public class ChatClient {
    private static final Logger log = LoggerFactory.getLogger(ChatClient.class);

    private Socket socket = null;
    private BufferedReader inputConsole = null;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public ChatClient(String address, int port) {
        try {
            socket = new Socket(address, port);
            log.info("Connected to the chat Server");

            inputConsole = new BufferedReader(new InputStreamReader(System.in));

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line = "";
            while (!line.equals("exit")) {
                line = inputConsole.readLine();
                out.println(line);
                log.info(in.readLine());
            }

            socket.close();
            inputConsole.close();
            out.close();

        } catch (UnknownHostException e) {
            log.warn("Host unknown: " + e.getMessage());
        } catch (IOException e) {
            log.warn("Unexpected exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient("127.0.0.1", 5000);
    }
}
