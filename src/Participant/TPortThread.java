package Participant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TPortThread extends Thread {
    private Long nportThreadID;
    private Socket skt;
    Boolean ifQuit = false;

    // constructor
    TPortThread(Socket skt, Long nportThreadID ) {
        this.nportThreadID = nportThreadID;
        this.skt = skt;
    }

    public void run() {
        try {
            DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
            DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                String recMsg = msgFromClient.readUTF();
            } while (!ifQuit);          // each client
            ifQuit = false;             // reset quit
        } catch (IOException exc) {
            //System.out.println("Broken pipe");
            //exc.printStackTrace();
        }

    }

}
