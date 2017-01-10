package chat;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private ConnectionThread connectionThread = new ConnectionThread();

    /**
     * Consumer interface represents an operation that accepts a single input argument and returns no result.
     * Serializable converts and objects state to byte stream to transfer the data, which is later converted back to the object.
    */

    private Consumer<Serializable> onReceiveCallback;

    /**
     * Public constructor of NetworkConnection with Daemon Thread,
     * prevents JVM from exiting when program finishes but the thread is still running.
    */

    public NetworkConnection(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connectionThread.setDaemon(true);
    }

    /**
     * Starts the connection thread
    */

    public void startConnection() throws Exception {
        connectionThread.start();
    }

    /**
     * Sends the data
    */

    public void send(Serializable data) throws Exception {
        connectionThread.objectOutputStream.writeObject(data);
    }

    /**
     * Closes the connection thread
    */

    public void closeConnection() throws Exception {
        connectionThread.socket.close();
        connectionThread.objectOutputStream.close();
    }


    /**
     * Using Thread for input and output so that, both perform simultaneously
    */

    private class ConnectionThread extends Thread {

        //Socket for connection between client and server
        private Socket socket;
        //For sending data
        private ObjectOutputStream objectOutputStream;


        @Override
        public void run() {

            /**
             * We will check if it is a server or not, if it is a server
             * only port is needed. Whereas for client both port and ip is required.
             * Afterwords we will create instances of input and ouput stream for data transfer.
            */

            try(ServerSocket serverSocket = isServer() ? new ServerSocket(getPort()) : null;
                Socket socket = isServer() ? serverSocket.accept() : new Socket(getIP(), getPort());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {

                this.socket = socket;
                this.objectOutputStream = objectOutputStream;
                socket.setTcpNoDelay(true); //For faster data transfer

                /**
                 * Until the connection is not closed the app will continue to run
                */

                while(true) {
                    Serializable data = (Serializable) objectInputStream.readObject();
                    onReceiveCallback.accept(data);
                }
            }
            catch (Exception e) {
                onReceiveCallback.accept("Connection Closed");
            }

        }

    }


    /**
     * All of these methods are overriden for Client and Server side
    */

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();
}
