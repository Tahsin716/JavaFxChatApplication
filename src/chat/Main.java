package chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Main extends Application {

    //Referring to the display TextArea
    @FXML
    TextArea display;
    //Referring to the input TextArea
    @FXML
    TextArea input;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("chat_ui.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 661, 531));
        primaryStage.show();
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
        return new Server(50000, data -> {
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
        return new Client("27.147.226.68", 50000, data -> {
            Platform.runLater(() -> {
                display.appendText(data.toString() + "\n");
            });
        });
    }

    /**
     * On send button clicked
     *
     * @param event
     */

    @FXML
    private void onSendClicked(ActionEvent event) {
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
