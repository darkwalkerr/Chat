package com.server.chatclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientController {
    @FXML
    private TextArea chatArea; // Область для отображения чата
    @FXML
    private TextField messageField; // Поле для ввода сообщения
    @FXML
    private Button sendButton; // Кнопка для отправки сообщения

    private PrintWriter out; // Поток для отправки данных серверу
    private BufferedReader in; // Поток для чтения данных от сервера

    public void initialize() {
        try {
            Socket socket = new Socket("localhost", 12345); //Сокет сервера  (его адрес и порт)
            out = new PrintWriter(socket.getOutputStream(), true); //: Поток для отправки данных серверу
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Поток для чтения данных от сервера

            Thread readerThread = new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        String finalMessage = message;
                        Platform.runLater(() -> {
                            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                            chatArea.appendText("Сервер [" + time + "] : " + finalMessage + "\n");
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            readerThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.setOnAction(event -> onSendMessageClick());
        messageField.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().toString().equals("ENTER")) {
                onSendMessageClick();
            }
        });
    }

    @FXML
    private void onSendMessageClick() {
        String message = messageField.getText();
        if (out != null && !message.isEmpty()) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            out.println(message);
            chatArea.appendText("Клиент [" + time + "] : " + message + "\n");
            messageField.clear();
        }
    }
}
