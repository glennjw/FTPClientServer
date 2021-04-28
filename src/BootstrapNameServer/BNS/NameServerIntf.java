package BootstrapNameServer.BNS;

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

        // for multi NS
        do {
            try {
                Socket nsIns = this.serverSocket.accept();
                // handle Msg or Table
                NameServerHandler nsHandler = new NameServerHandler( nsIns, nsContn );
                Thread nsHandlerThr = new Thread(nsHandler);
                nsHandlerThr.start();


            } catch (IOException exc) {
                System.out.println("NS connection failed.");
                //System.exit(1);
            }
        } while (true);                 // run once

    }

}
