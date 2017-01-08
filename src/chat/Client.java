package chat;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {

    private int portNumber;
    private String IP;

    /**
     * Public constructor for Client, super(onReceiveCallback) is used to call the
     * constructor of the super class.
     */

    public Client(String IP, int portNumber, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.IP = IP;
        this.portNumber = portNumber;
    }


    /**
     * Abstract methods overriden
     */


    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return IP;
    }

    @Override
    protected int getPort() {
        return portNumber;
    }
}
