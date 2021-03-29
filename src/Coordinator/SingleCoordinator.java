package Coordinator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleCoordinator {

    int nPort = 2121;
    int tPort = 2222;
    ServerSocket nportServerSkt;
    ServerSocket tportServerSkt;
    Socket nportSkt;
    Socket tportSkt;

    SingleCoordinator() {

    }

    public void run() throws IOException {
        nportServerSkt = new ServerSocket(nPort);
        tportServerSkt = new ServerSocket(tPort);

        do {
            System.out.println("waiting for new client");
            // try connect
            try {
                nportSkt = nportServerSkt.accept();
                tportSkt = tportServerSkt.accept();
                System.out.println("New client connected.");
                Thread nportThread = new NPortThread(nportSkt);     // nport thread
                nportThread.start();
                Thread tportThread = new TPortThread( tportSkt, nportThread.getId() );     // tport thread
                tportThread.start();
                System.out.println("nport tport id: " + Long.toString(nportThread.getId()) + " " + Long.toString(tportThread.getId()) );
            } catch (IOException exc) {
                System.out.println("Connection failed.");
                System.exit(1);
            }
        } while (true);                 // keep server up
    }

}
