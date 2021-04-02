package Participant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NPortThread extends Thread {
    String coorIP;
    Socket nportSkt;
    Socket tportSkt;
    String partiID;
    String logPath;
    String cmdToCoor;
    String responseMsg = "";
    Boolean ifQuit = false;
    Boolean openPort = true;

    public NPortThread(String coorIP, Socket nportSkt, String partiID, String logPath) {
        this.coorIP = coorIP;
        this.nportSkt = nportSkt;
        this.partiID = partiID;
        this.logPath = logPath;
    }

    public void run( ) {
        DataInputStream recNportSkt = null;
        DataOutputStream sendNportSkt = null;

        // connect to server
        try {
            recNportSkt = new DataInputStream(nportSkt.getInputStream());
            sendNportSkt = new DataOutputStream(nportSkt.getOutputStream());
        } catch (IOException eIO) {
            System.out.println("Connection failed.");
            System.exit(0);
        }

        // input cmd
        Scanner cmdRec = new Scanner(System.in);
        String responseMsg = "";
        do {
            // input cmd
            System.out.print("Cmd> ");
            String input = cmdRec.nextLine();

            // execute cmd
            try {
                cmdInterface(input, recNportSkt, sendNportSkt);
                if ("" != cmdToCoor) {
                    sendNportSkt.writeUTF(cmdToCoor);
                    System.out.println(responseMsg = recNportSkt.readUTF());
                } else {
                    System.out.println("Pls check command!");
                }
                //checkMsg(responseMsg);

            } catch (Exception exc) {}

            cmdToCoor = "";     // reset msgToServer

        } while (openPort);
    }

    private void cmdInterface(String input, DataInputStream msgRec, DataOutputStream msgSend) throws IOException {
        String cmd = "";
        String para = "";
        //List<String> para = new ArrayList<String>();
        // split and assign input to cmd and para
        input = input.trim().toLowerCase();
        if (-1 < input.indexOf(" ")) {
            String[] inputSplited = input.split(" ");
            cmd = inputSplited[0].toLowerCase();
            para = inputSplited[1];
            //for (int i=1; i < inputSplited.length; i++) { para.add(inputSplited[i]); }

        } else {
            cmd = input;
        }
        // check cmd
        switch (cmd) {
            default:
                cmdToCoor = "";
                break;
            case "register":
                cmdRegister(para);
                break;
            case "deregister":
                cmdDeregister(para);
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

    private void cmdRegister(String para) throws UnknownHostException {
        cmdToCoor = "register " + partiID + " " + Inet4Address.getLocalHost().getHostAddress() + " " + para;
    }

    private void cmdDeregister(String para) {
        cmdToCoor = "deregister " + partiID;
    }

    public void cmdDisconnect(String para) {
        cmdToCoor = "disconnect " + partiID;
    }

    public void cmdReconnect(String para) throws UnknownHostException {
        cmdToCoor = "reconnect " + partiID + " " + Inet4Address.getLocalHost().getHostAddress() + " " + para;
    }

    public void cmdMsend(String path) {

    }


}