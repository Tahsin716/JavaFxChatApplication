package chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


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
        display.setPrefHeight(450);
        display.setEditable(false);

        TextField input = new TextField();
        input.setPrefHeight(50);
        input.setPrefWidth(300);

        Button send = new Button("Send");
        send.setAlignment(Pos.CENTER);
        send.setOnAction(event -> {
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

        VBox root = new VBox(20, display, input, send);
        root.setMargin(input, new Insets(0,5,0,5));
        root.setMargin(send, new Insets(20,0,50,270));
        root.setPrefSize(600, 600);
        return root;
    }

    //Determines whether server of client is to be created
    public boolean isServer = true;


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
     * Launches the program
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }
}
