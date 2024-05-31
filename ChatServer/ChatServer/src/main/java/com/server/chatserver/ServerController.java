package com.server.chatserver;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerController {
    @FXML
    private TextArea chatArea; // Область для отображения чата
    @FXML
    private TextField messageField; // Поле для ввода сообщения
    @FXML
    private Button sendButton; // Кнопка для отправки сообщения

    private PrintWriter out; // Поток для отправки данных клиенту
    private BufferedReader in; // Поток для чтения данных от клиента
    private Socket clientSocket; // Сокет клиента

    public void initialize() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                Platform.runLater(() -> chatArea.appendText("Сервер запущен...\n"));
                clientSocket = serverSocket.accept(); //Сокет клиента
                out = new PrintWriter(clientSocket.getOutputStream(), true);  //Поток для отправки данных клиенту
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));  //Поток для чтения данных от клиента

                Platform.runLater(() -> chatArea.appendText("Клиент подключен...\n"));

                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message;
                    Platform.runLater(() -> {
                        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                        chatArea.appendText("Клиент [" + time + "] : " + finalMessage + "\n");
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

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
            chatArea.appendText("Сервер [" + time + "] : " + message + "\n");
            messageField.clear();
        }
    }
}
