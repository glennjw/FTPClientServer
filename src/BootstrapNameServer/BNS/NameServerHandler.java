package BootstrapNameServer.BNS;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * handle new conn from new NS
 */

public class NameServerHandler implements Runnable {
    Socket nsSocket;
    NameServerContainer nsContn;
    String response = "";

    public NameServerHandler(Socket nsSocket, NameServerContainer nsContn) {
        this.nsSocket = nsSocket;
        this.nsContn = nsContn;
    }

    public void run() {
        DataInputStream msgFromClient = null;
        DataOutputStream msgToClient = null;
        String recMsg = null;
        try {
            msgFromClient = new DataInputStream(nsSocket.getInputStream());
            msgToClient = new DataOutputStream(nsSocket.getOutputStream());
            recMsg = msgFromClient.readUTF();
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
            case "enter":
                cmdEnter(para);
                break;
            case "exit":
                cmdExit(para);
                break;
        }
    }

    public void cmdEnter(ArrayList<String> recMsg) throws IOException {
        // recMsg = [ ID, port ]
        Integer newNSId = Integer.parseInt((String) recMsg.get(0));
        String newNSIP = nsSocket.getInetAddress().getHostAddress();
        Integer newNSPort = Integer.parseInt((String) recMsg.get(1));
        String formatedMsg = "enter " + newNSId + " " + newNSIP + " " + newNSPort.toString();

        if ( null == nsContn.successor && newNSId > nsContn.nsId ) {      // add newNS to successor
            NameServerAddr newNS = new NameServerAddr( newNSIP, newNSPort );
            nsContn.successor = newNS;
            String informPredMsg = "newPredcessor " + nsContn.addr.port;
            SendMsg informPred = new SendMsg(newNSIP, newNSPort, false, informPredMsg);
            informPred.send();
            List<String> newNSTable = nsContn.table.slice(nsContn.tableHead, newNSId);    // split table
            String addTableMsg = "addTable " + Arrays.toString(newNSTable.toArray()).replace(",", "").replace("[", "").replace("]", "");
            SendMsg sendTable = new SendMsg(newNSIP, newNSPort, false, addTableMsg);
            sendTable.send();
        } else if ( newNSId > nsContn.nsId ) {                            // if newId > this.ID, pass to next NS
            // send new NS info to next NS
            SendMsg sendMsg = new SendMsg( newNSIP, newNSPort, false , formatedMsg);
            sendMsg.send();
        } else if ( newNSId < nsContn.nsId ) {                            // if newId < successor, insert NS
            // send successor info to change successor's predecessor
            SendMsg sendMsg = new SendMsg( newNSIP, newNSPort, false , formatedMsg );
            sendMsg.send();         // next is split table

            // set this.successor
            //nsContn.successor =
            // reply new ns: predecessor and successor

        }

    }

    public void cmdExit (ArrayList<String> para) {
        // para is NS id

    }

}
