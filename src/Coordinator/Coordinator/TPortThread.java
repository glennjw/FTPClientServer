package Coordinator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TPortThread extends Thread {
    private Socket skt;
    PartiGroup partiGroup;
    String msgNow;

    // constructor
    public TPortThread(Socket skt, PartiGroup partiGroup, String msgNow) {
        this.partiGroup = partiGroup;
        this.skt = skt;
        this.msgNow = msgNow;
    }

    public void run() {

        while (!"".equals(msgNow)) {
            try {
                sendMsg(msgNow);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void sendMsg(String msgNow) throws IOException {

        //DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
        DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
        //String recMsg = msgFromClient.readUTF();
        //partiGroup.sendMsg(msgToClient, msgNow);         // send msg to all parti


    }

}
