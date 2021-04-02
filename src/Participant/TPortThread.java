package Participant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TPortThread extends Thread {
    String coorIP;
    Socket skt;
    Boolean ifQuit = false;

    public TPortThread(String coorIP, Socket skt ) {
        this.coorIP = coorIP;
        this.skt = skt;
    }

    public void run() {
        try {
            DataInputStream msgRec = new DataInputStream(skt.getInputStream());
            DataOutputStream msgSend = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                String recMsg = msgRec.readUTF();
            } while (!ifQuit);          // each client
            ifQuit = false;             // reset quit
        } catch (IOException exc) {
            //System.out.println("Broken pipe");
            //exc.printStackTrace();
        }

    }

}
