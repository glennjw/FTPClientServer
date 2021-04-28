package NameServer.NS;

import NameServer.NS.NameServerContainer;
import NameServer.NS.NameServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * dispatch new NS conn
 */

public class NameServerIntf implements Runnable {

    ServerSocket serverSocket;
    NameServerContainer nsContn;

    NameServerIntf(ServerSocket nportServerSkt, NameServerContainer nsContn ) {
        this.serverSocket = nportServerSkt;
        this.nsContn = nsContn;
    }

    public void run() {
        do {
            try {
                Socket nsIns = this.serverSocket.accept();
                // handle Msg or Table
                NameServerHandler nsHandler = new NameServerHandler( nsIns, nsContn );
                nsHandler.run();
                nsIns.close();
            } catch (IOException exc) {
                System.out.println("NS connection failed.");
                //System.exit(1);
            }
        } while (true);                 // run once

    }

}
