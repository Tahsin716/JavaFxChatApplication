package chat;

import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class NetworkConnection {


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

        }

    }


    /**
     * All of these methods are overriden for Client and Server side
    */

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();
}
