package com.example.labb2;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.*;
import java.nio.file.Files;

public class VigenereCipherApp extends Application {

    private TextArea textArea;
    private TextField keyField;
    private File currentFile;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Шифр Віженера");

        textArea = new TextArea();
        keyField = new TextField();
        keyField.setPromptText("Введіть ключ для шифрування");

        Button openFileButton = new Button("Відкрити файл");
        openFileButton.setOnAction(e -> openFile(primaryStage));

        Button encryptButton = new Button("Зашифрувати");
        encryptButton.setOnAction(e -> encryptText());

        Button decryptButton = new Button("Розшифрувати");
        decryptButton.setOnAction(e -> decryptText());

        Button clearKeyButton = new Button("Очистити ключ");
        clearKeyButton.setOnAction(e -> keyField.clear());

        VBox layout = new VBox(10, openFileButton, keyField, encryptButton, decryptButton, clearKeyButton, textArea);
        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстові файли", "*.txt"),
                new FileChooser.ExtensionFilter("Документи Word", "*.docx")
        );
        currentFile = fileChooser.showOpenDialog(stage);

        if (currentFile != null) {
            try {
                String content = new String(Files.readAllBytes(currentFile.toPath()));
                textArea.setText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encryptText() {
        String key = keyField.getText();
        String text = textArea.getText();
        String encryptedText = vigenereCipher(text, key, true);
        textArea.setText(encryptedText);
    }

    private void decryptText() {
        String key = keyField.getText();
        String text = textArea.getText();
        String decryptedText = vigenereCipher(text, key, false);
        textArea.setText(decryptedText);
    }

    private String vigenereCipher(String text, String key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        key = key.toUpperCase();
        int keyIndex = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int offset = (c - base + (encrypt ? 1 : -1) * (key.charAt(keyIndex) - 'A') + 26) % 26;
                result.append((char) (base + offset));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}