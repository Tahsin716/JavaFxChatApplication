package chat;

import java.io.Serializable;
import java.util.function.Consumer;

public class Server extends NetworkConnection {

    private int portNumber;

    /**
     * Public constructor for Server, super(onReceiveCallback) is used to call the
     * constructor of the super class.
    */

    public Server(int portNumber, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.portNumber = portNumber;
    }


    /**
     * Abstract methods overriden
    */

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return portNumber;
    }
}
