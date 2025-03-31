package com.example.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private JTextField recipientField;
    private ChatClient client;
    private String currentUser;

    public ChatClientGUI() {
        super("Chat Application");
        initializeAuthDialog();
        setupMainUI();
    }

    private void initializeAuthDialog() {
        JDialog authDialog = new JDialog(this, "Authentication", true);
        authDialog.setLayout(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        authDialog.add(new JLabel("Username:"));
        authDialog.add(usernameField);
        authDialog.add(new JLabel("Password:"));
        authDialog.add(passwordField);
        authDialog.add(new JLabel());
        authDialog.add(loginButton);

        loginButton.addActionListener(e -> {
            try {
                this.client = new ChatClient(this::onMessageReceived);
                boolean success = client.login(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        "/login"
                );

                if (success) {
                    currentUser = usernameField.getText();
                    authDialog.dispose();
                    client.startClient();
                } else {
                    success = client.login(
                            usernameField.getText(),
                            new String(passwordField.getPassword()),
                            "signup"
                    );
                    if (success) {
                        currentUser = usernameField.getText();
                        authDialog.dispose();
                        client.startClient();
                    } else {
                        JOptionPane.showMessageDialog(authDialog,
                                "Invalid credentials",
                                "Auth Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                handleError(ex);
            }
        });

        authDialog.pack();
        authDialog.setLocationRelativeTo(null);
        authDialog.setVisible(true);
    }

    private void setupMainUI() {
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Панель получателя
        JPanel recipientPanel = new JPanel(new BorderLayout());
        recipientPanel.add(new JLabel("To:"), BorderLayout.WEST);
        recipientField = new JTextField();
        recipientPanel.add(recipientField, BorderLayout.CENTER);
        add(recipientPanel, BorderLayout.NORTH);

        // Основная область сообщений
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Панель ввода сообщения
        JPanel inputPanel = new JPanel(new BorderLayout());
        textField = new JTextField();
        textField.addActionListener(this::sendMessage);
        inputPanel.add(textField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(this::sendMessage);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void sendMessage(ActionEvent e) {
        try {
            client.sendMessage(
                    textField.getText(),
                    currentUser,
                    recipientField.getText()
            );
            textField.setText("");
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() ->
                messageArea.append(message + "\n")
        );
    }

    private void handleError(Exception ex) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI gui = new ChatClientGUI();
            gui.setVisible(true);
        });
    }
}