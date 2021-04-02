package Coordinator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NPortThread extends Thread {
    private Socket skt;
    ArrayList<ArrayList<String>> partiGroup;
    String response = "";
    Boolean ifQuit = false;



    public NPortThread(Socket skt, ArrayList<ArrayList<String>> partiGroup) {
        this.skt = skt;
        this.partiGroup = partiGroup;

    }

    public void run() {
        try {
            DataInputStream msgFromClient = new DataInputStream(skt.getInputStream());
            DataOutputStream msgToClient = new DataOutputStream(skt.getOutputStream());
            // execute cmd
            do {
                String recMsg = msgFromClient.readUTF();
                System.out.println( "cmd is: " + recMsg );
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
                System.out.println("Command not found.");
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
        // remove client if "deregistered"
        // [ ID, IP, port#, status, msg]
        if ( ( partiGroup.stream().filter(parti -> parti.get(0).equals(para.get(0))).collect(Collectors.toList()) ).isEmpty() ) {
            para.add("registered");
            partiGroup.add( para );
        } else {
            for (int i=0; i<partiGroup.size(); i++) {
                if ( para.get(0).equals(partiGroup.get(i).get(0)) ) {
                    partiGroup.get(i).set(3, "registered");
                }
            }
        }
        response = "";
        System.out.println(partiGroup);
    }


    private void cmdDeregister( ArrayList<String> para, DataInputStream msgFromClient) {
        // remove client if "deregistered"
        // [ ID, IP, port#, status, msg]
        for ( int i=0; i<partiGroup.size(); i++) {
            if ( partiGroup.get(i).get(0).equals( para.get(0) ) ) { partiGroup.remove(i); }
        }
        response = "";
        System.out.println(partiGroup);
    }

    public void cmdDisconnect(ArrayList<String> para) {
        // [ ID, IP, port#, status, msg]
        for ( int i=0; i<partiGroup.size(); i++) {
            if ( partiGroup.get(i).get(0).equals( para.get(0) ) ) { partiGroup.get(i).set(3,"disconnected"); }
        }
        response = "";
        System.out.println(partiGroup);
    }

    public void cmdReconnect(ArrayList<String> para) {
        // [ ID, IP, port#, status, msg]
        for ( int i=0; i<partiGroup.size(); i++) {
            if ( partiGroup.get(i).get(0).equals( para.get(0) ) ) {
                partiGroup.get(i).set(1,para.get(1));
                partiGroup.get(i).set(2,para.get(2));
                partiGroup.get(i).set(3,"connected");
            }
        }
        response = "";
        System.out.println(partiGroup);
    }

    public void cmdMsend(List<String> path) {

    }


}
