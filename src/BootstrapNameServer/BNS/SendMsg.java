package BootstrapNameServer.BNS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendMsg {
    NameServerAddr nsAddr;
    String msg;
    Boolean needReply = false;

    SendMsg ( String IP, Integer port, Boolean needReply, String msg) {
        this.nsAddr = new NameServerAddr(IP, port);
        this.msg = msg;
        this.needReply = needReply;
    }

    public String send () throws IOException {
        String response = null;
        DataInputStream recNportSkt = null;
        DataOutputStream sendNportSkt = null;
        Socket nportSkt = null;
        try {
            nportSkt = new Socket(nsAddr.IP, nsAddr.port);
            recNportSkt = new DataInputStream(nportSkt.getInputStream());
            sendNportSkt = new DataOutputStream(nportSkt.getOutputStream());
        } catch (
                IOException eIO) {
            System.out.println("Connect NS: " + nsAddr.IP + " " + nsAddr.port + " failed.");
            //System.exit(0);
        }
        sendNportSkt.writeUTF(msg);
        response = needReply ? recNportSkt.readUTF() : null;
        nportSkt.close();
        return response;

    }
}
