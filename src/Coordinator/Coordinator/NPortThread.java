package Coordinator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NPortThread extends Thread {
    private Socket skt;
    PartiGroup partiGroup;
    String partiID;
    String response = "";
    Boolean ifQuit = false;
    String msgNow = "";
    Integer tPort;



    public NPortThread(Socket skt, PartiGroup partiGroup, Integer tPort) {
        this.skt = skt;
        this.partiGroup = partiGroup;
        this.tPort = tPort;

    }

    public void run() {
        try {
            DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
            DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                String recMsg = msgFromClient.readUTF();
                cmdInterface(recMsg, msgFromClient, msgToClient);
                msgToClient.writeUTF(response);

                // if end session
                if (true == ifQuit) {
                    msgFromClient.close();
                    msgToClient.close();
                    skt.close();
                }
                response = "";           // reset response
            } while (!ifQuit);          // each client
            ifQuit = false;             // reset quit
        } catch (IOException exc) {
            //exc.printStackTrace() ;
        }
    }

    private void cmdInterface(String input, DataInputStream msgFromClient, DataOutputStream msgToClient) throws IOException {
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
            case "register":
                cmdRegister(para, msgToClient);
                break;
            case "deregister":
                cmdDeregister(para, msgFromClient);
                break;
            case "disconnect":
                cmdDisconnect(para);
                break;
            case "reconnect":
                cmdReconnect(para);
                break;
            case "msend":
                cmdMsend(para);
                break;
        }
    }

    private void cmdRegister( ArrayList<String> para, DataOutputStream msgToClient) {
        // para: [ ID, IP, port# ]
        if ( !partiGroup.has( para.get(0) ) ) {
            partiGroup.add( new Parti( para.get(0), para.get(1), Integer.parseInt(para.get(2))) );
        } else {
            partiID = para.get(0);
            Parti parti = partiGroup.use( para.get(0) );
            parti.IP = para.get(1);
            parti.port = Integer.parseInt(para.get(2));
        }
        response = "";
    }

    private void cmdDeregister( ArrayList<String> para, DataInputStream msgFromClient) {
        // [ ID, IP, port# ]
        if ( partiGroup.has( para.get(0) )) { partiGroup.remove(para.get(0)); }
        partiID = "";
        response = "";
    }

    public void cmdDisconnect(ArrayList<String> para) {
        // [ ID, IP, port# ]
        if ( partiGroup.has(para.get(0))) {
            response = partiGroup.disconn(para.get(0)) ? "" : "Failed to disconnect";
        }
    }

    public void cmdReconnect( ArrayList<String> para ) throws IOException {
        // [ ID, IP, port# ]
        for ( Parti parti : partiGroup ) {
            if ( parti.ID.equals(para.get(0)) ) {
                parti.IP = para.get(1);
                parti.port = Integer.parseInt(para.get(2));
                parti.status = "registered";
                partiGroup.sendMsgToIdv( tPort, parti.ID );
            }
        }
        response = "";
    }

    public void cmdMsend(ArrayList<String> msg) throws IOException {
        for (int i=1; i<msg.size(); i++) { msgNow += msg.get(i)+" "; }
        msgNow.trim();
        partiGroup.sendMsgToGroup( tPort, msgNow );
        msgNow = "";
    }


}
