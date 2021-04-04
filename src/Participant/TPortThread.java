package Participant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TPortThread extends Thread {
    Integer tPort;
    Boolean ifQuit = false;

    public TPortThread(Integer tPort) {
        this.tPort = tPort;
    }

    public void run() {
        try {
            String recMsg;
            ServerSocket tportServerSkt = new ServerSocket(tPort);
            Socket skt = tportServerSkt.accept();
            DataInputStream msgRec = new DataInputStream(skt.getInputStream());
            DataOutputStream msgSend = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                recMsg = msgRec.readUTF();
                ifQuit = !"".equals(recMsg);
            } while (!ifQuit);          // each client
            System.out.println("Incoming: " + recMsg );

        } catch (IOException exc) {
            //System.out.println("Broken pipe");
            //exc.printStackTrace();
        }

    }

}
