package chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    TextArea display;

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
    public static boolean isServer = false;


    //Connection should be either server or client
    public  NetworkConnection connection = isServer ? createServer() : createClient();


    /**
     * Creates connection as server
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
     * Launches the program
     *
     * @param args
     */

    public static void main(String[] args) {
        launch(args);
    }
}
