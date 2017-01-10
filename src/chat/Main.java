package chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    private TextArea display = new TextArea();


    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Parent createContent() {
        display.setFont(Font.font(14));
        display.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            display.appendText(message + "\n");

            try {
                connection.send(message);
            }
            catch (Exception e) {
                display.appendText("Failed to send\n");
            }
        });

        VBox root = new VBox(20, display, input);
        root.setPrefSize(600, 600);
        return root;
    }

    //Determines whether server of client is to be created
    private  boolean isServer = false;


    //Connection should be either server or client
    public  NetworkConnection connection = isServer ? createServer() : createClient();


    /**
     * Creates connection as server
     * Platform.runLater(): If you need to update a GUI component from a non-GUI thread,
     * you can use that to put your update in a queue and it will be handled by the GUI thread as soon as possible.
     *
     * @return
     */

    private Server createServer() {
        return new Server(55555, data -> {
            Platform.runLater(() -> {
                display.appendText(data.toString() + "\n");
            });
        });
    }

    /**
     * Creates connection as client
     *
     * @return
     */

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            Platform.runLater(() -> {
                display.appendText(data.toString() + "\n");
            });
        });
    }


    /**
     * Dialog box for confirming whether user wants to create
     * server or a client
     */

    private void showDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog with Custom Actions");
        alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
        alert.setContentText("Choose your option.");

        ButtonType buttonServer = new ButtonType("Server");
        ButtonType buttonClient = new ButtonType("Client");
        ButtonType buttonCancel = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(buttonServer, buttonClient, buttonCancel);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == buttonServer) { isServer = true; }
        else if (result.get() == buttonClient) { isServer = false; }
        else { System.exit(0); }
    }


    /**
     * Launches the program
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }
}
