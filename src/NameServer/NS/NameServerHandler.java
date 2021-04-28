package NameServer.NS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * handle new conn from new NS
 */

public class NameServerHandler implements Runnable {
    Socket nsSocket;
    NameServerContainer nsContn;
    String response = "";

    NameServerHandler(Socket nsSocket, NameServerContainer nsContn) {
        this.nsSocket = nsSocket;
        this.nsContn = nsContn;
    }

    public void run() {
        DataInputStream msgRecv = null;
        DataOutputStream msgSend = null;
        String recMsg = null;
        try {
            msgRecv = new DataInputStream(nsSocket.getInputStream());
            msgSend = new DataOutputStream(nsSocket.getOutputStream());
            recMsg = msgRecv.readUTF();
            cmdInterface( recMsg );

        } catch (IOException e) {
            //e.printStackTrace();
        }


    }

    private void cmdInterface(String input) throws IOException {
        String cmd = "";
        ArrayList<String> para = new ArrayList<>();
        // split and assign input to cmd and para
        input = input.trim();
        if (-1 < input.indexOf(" ")) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0].toLowerCase();
            for (int i=1; i<inputSplited.length; i++) { para.add(inputSplited[i]);}
        } else {
            cmd = input;
        }
        // check cmd
        switch (cmd) {
            default:
                System.out.println("Command not supported.");
                break;
            case "newpredcessor":
                cmdNewpredcessor(para);
                break;
            case "addtable":
                cmdAddtable(para);
                break;

        }
    }

    public void cmdNewpredcessor (ArrayList<String> para) {
        // [ port ]
        nsContn.predcessor = new NameServerAddr( nsSocket.getInetAddress().getHostAddress(), Integer.parseInt(para.get(0)) );

    }

    public void cmdAddtable (ArrayList<String> para) {
        nsContn.table.append( para );
    }

}
