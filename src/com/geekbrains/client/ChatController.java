package com.geekbrains.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private TextField messageField, loginField;
    @FXML
    private HBox messagePanel, authPanel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ListView<String> clientList;


    private MessageHistory messageHistory;


    private final Network network;

    public ChatController() {
        this.network = new Network(this);
        try {
            if(new File("chatHistory.txt").exists()) {
                FileInputStream fileInputStream = new FileInputStream("chatHistory.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                messageHistory = (MessageHistory) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } else
                this.messageHistory = new MessageHistory(new LinkedList<>());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setAuthenticated(boolean authenticated) {
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        messagePanel.setVisible(authenticated);
        messagePanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
        if(authenticated) {
            if(messageHistory.getMessages().size() >= 100) {
                for (int i = messageHistory.getMessages().size() - 100; i < messageHistory.getMessages().size(); i++) {
                    displayMessage(messageHistory.getMessage(i), true);
                }
            } else {
                for (int i = 0; i < messageHistory.getMessages().size(); i++) {
                    displayMessage(messageHistory.getMessage(i), true);
                }
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAuthenticated(false);
    }

    public void displayMessage(String text, boolean isRepit) {
        if (textArea.getText().isEmpty()) {
            textArea.setText(text);
            messageHistory.addMessage(text);
        } else {
            textArea.setText(textArea.getText() + "\n" + text);
            if(!isRepit)
                messageHistory.addMessage(text);
        }
    }

    public void displayClient(String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().add(nickName);
            }
        });
    }

    public void removeClient(String nickName) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clientList.getItems().remove(nickName);
            }
        });
    }


    public void sendAuth(ActionEvent event) {
        boolean authenticated = network.sendAuth(loginField.getText(), passwordField.getText());
        if(authenticated) {
            loginField.clear();
            passwordField.clear();
            setAuthenticated(true);
        }
    }

    public void sendMessage(ActionEvent event) {
        network.sendMessage(messageField.getText());
        messageField.clear();
    }

    public void close() {
        try {
            FileOutputStream outputStream = new FileOutputStream("chatHistory.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(messageHistory);
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        network.closeConnection();
    }
}
