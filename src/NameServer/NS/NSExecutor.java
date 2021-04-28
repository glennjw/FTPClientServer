package NameServer.NS;

import NameServer.NS.NameServerContainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NSExecutor implements Runnable {

    ServerSocket sktServer;
    String response = null;
    Integer nPort;
    NameServerContainer nsContn;
    Boolean keepUp = true;

    public NSExecutor(ServerSocket nportServerSkt, NameServerContainer nsContn ) {
        this.sktServer = nportServerSkt;
        //this.partiIP = skt.getInetAddress().getHostAddress();
        this.nPort = nportServerSkt.getLocalPort();
        this.nsContn = nsContn;
    }

    public void run() {

        Scanner cmdRec = new Scanner(System.in);

        do {
            System.out.print("Cmd> ");
            String input = cmdRec.nextLine();        // input cmd
            try {
                cmdInterface(input);
                if (null!=response) {System.out.println( response );}
            } catch (IOException | InterruptedException e) {
                System.out.println("Cmd failed.");
            }

        } while (keepUp);
    }

    private void cmdInterface(String input) throws IOException, InterruptedException {
        String cmd = input.trim().toLowerCase();

        // check cmd
        switch (cmd) {
            default:
                //System.out.println("Command not supported.");
                break;
            case "enter":
                cmdEnter();
                break;
            case "exit":
                cmdExit();
                break;
            case "quit":
                System.exit(0);
                break;
        }
    }

    private void cmdEnter( ) throws IOException, InterruptedException {
        // send "enter" to BNS
        String msg = "enter " + nsContn.nsId.toString() + " " + nsContn.port;
        SendMsg msgBns = new SendMsg(nsContn.bns.IP, nsContn.bns.port, false, msg);
        String response = msgBns.send();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Successful entry");

    }

    private void cmdExit( ) {

    }

    public void trfStart (Boolean bg) {
        // bg traffic thread

    }

    public void operByKey(String oper, Integer key) throws IOException {

        /**
        DataInputStream recNportSkt = null;
        DataOutputStream sendNportSkt = null;
        for (NameServerContainer ns:allNsContn) {
            if ( key >= ns.tableHead && key <= ns.tableTail ) {
                try {
                    Socket nportSkt = new Socket( ns.IP, ns.port );
                    recNportSkt = new DataInputStream(nportSkt.getInputStream());
                    sendNportSkt = new DataOutputStream(nportSkt.getOutputStream());
                } catch (IOException eIO) {
                    System.out.println("Connect NS failed.");
                    //System.exit(0);
                }
                sendNportSkt.writeUTF(oper+ " " + key.toString());
                response = recNportSkt.readUTF();

        }
         */
    }


}
